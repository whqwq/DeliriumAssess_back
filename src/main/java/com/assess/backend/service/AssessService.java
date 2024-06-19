package com.assess.backend.service;

import com.assess.backend.DTO.GetAssess.AssessDTO;
import com.assess.backend.DTO.GetAssess.AssessRecordDTO;
import com.assess.backend.DTO.GetAssessRecordDetails.RecordDetailsDTO;
import com.assess.backend.entity.*;
import com.assess.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AssessService {
    private final AssessmentRepository assessmentRepository;
    private final AssessRecordRepository assessRecordRepository;
    private final RecordQuestionRepository recordQuestionRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final DoctorProjectRepository doctorProjectRepository;
    @Autowired
    public AssessService(AssessmentRepository assessmentRepository, AssessRecordRepository assessRecordRepository, RecordQuestionRepository recordQuestionRepository, PatientRepository patientRepository, UserRepository userRepository, DoctorProjectRepository doctorProjectRepository) {
        this.assessmentRepository = assessmentRepository;
        this.assessRecordRepository = assessRecordRepository;
        this.recordQuestionRepository = recordQuestionRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.doctorProjectRepository = doctorProjectRepository;
    }
    public ResponseEntity<APIResponse> getAssess(Map<String, Object> data, String phone) {
        User user = userRepository.findByPhone(phone);
        if (user == null) {
            return ResponseEntity.ok(new APIResponse(1, "User not found"));
        }
        long userId = user.getId();
        long assessmentId = Long.parseLong(data.get("assessmentId").toString());
        Assessment assessment = assessmentRepository.findById(assessmentId);
        if (assessment == null) {
            return ResponseEntity.ok(new APIResponse(2, "Assessment not found"));
        }
        long patientId = assessment.getPatientId();
        Patient patient = patientRepository.findById(patientId);
        if (patient == null) {
            return ResponseEntity.ok(new APIResponse(3, "Patient not found"));
        }
        String projectId = patient.getProjectId();
        if (doctorProjectRepository.findByProjectIdAndDoctorId(projectId, userId) == null) {
            return ResponseEntity.ok(new APIResponse(4, "Permission denied"));
        }
        long assessorId = assessment.getAssessorId();
        User assessor = userRepository.findById(assessorId);
        if (assessor == null) {
            return ResponseEntity.ok(new APIResponse(5, "Assessor not found"));
        }
        // 开始构造返回数据
        AssessDTO assessDTO = new AssessDTO(assessment.getAssessTime(), assessment.getAssessType(), patientId, patient.getAlpha(), patient.getPatientIdInProject(), assessor.getName(), assessor.getPhone());
        List<AssessRecord> assessRecords = assessRecordRepository.findAllByAssessmentIdOrderByRecordTimeDesc(assessmentId);
        List<AssessRecordDTO> assessRecordDTOs = new ArrayList<>();
        for (int i = 0; i < assessRecords.size(); i++) {
            AssessRecord assessRecord = assessRecords.get(i);
            long recorderId = assessRecord.getRecorderId();
            User recorder = userRepository.findById(recorderId);
            if (recorder == null) {
                return ResponseEntity.ok(new APIResponse(4, "Recorder not found"));
            }
            AssessRecordDTO assessRecordDTO = new AssessRecordDTO(assessRecord.getId(), assessRecord.getRecordTime(), recorder.getPhone(), recorder.getName(),  assessRecord.getIsOriginal(),i==0, assessRecord.getFeature1Positive(), assessRecord.getFeature2Positive(), assessRecord.getFeature3Positive(), assessRecord.getFeature4Positive());
            assessRecordDTOs.add(assessRecordDTO);
        }
        return ResponseEntity.ok(new APIResponse(Map.of("assessment", assessDTO, "records", assessRecordDTOs)));
    }
    public ResponseEntity<APIResponse> getAssessRecordDetails(Map<String, Object> data, String phone) {
        User user = userRepository.findByPhone(phone);
        if (user == null) {
            return ResponseEntity.ok(new APIResponse(1, "User not found"));
        }
        long userId = user.getId();
        long recordId = Long.parseLong(data.get("recordId").toString());
        AssessRecord assessRecord = assessRecordRepository.findById(recordId);
        if (assessRecord == null) {
            return ResponseEntity.ok(new APIResponse(2, "AssessRecord not found"));
        }
        long assessmentId = assessRecord.getAssessmentId();
        Assessment assessment = assessmentRepository.findById(assessmentId);
        if (assessment == null) {
            return ResponseEntity.ok(new APIResponse(3, "Assessment not found"));
        }
        long patientId = assessment.getPatientId();
        Patient patient = patientRepository.findById(patientId);
        if (patient == null) {
            return ResponseEntity.ok(new APIResponse(4, "Patient not found"));
        }
        String projectId = patient.getProjectId();
        if (doctorProjectRepository.findByProjectIdAndDoctorId(projectId, userId) == null) {
            return ResponseEntity.ok(new APIResponse(5, "Permission denied"));
        }
        long recorderId = assessRecord.getRecorderId();
        User recorder = userRepository.findById(recorderId);
        if (recorder == null) {
            return ResponseEntity.ok(new APIResponse(6, "Recorder not found"));
        }

        // 开始构造返回数据
        List<AssessRecord> assessRecords = assessRecordRepository.findAllByAssessmentIdOrderByRecordTimeDesc(assessmentId);
        Boolean isLatest = assessRecords.get(0).getId() == recordId;
        List<RecordQuestion> recordQuestions = recordQuestionRepository.findAllByRecordId(recordId);
        RecordDetailsDTO recordDetailsDTO = new RecordDetailsDTO(recordId, assessRecord.getRecordTime(), assessment.getAssessType(), patientId, patient.getPatientIdInProject(), patient.getAlpha(), recorder.getPhone(), recorder.getName(), assessRecord.getChangeReason(), isLatest, recordQuestions);
        return ResponseEntity.ok(new APIResponse(Map.of("record", recordDetailsDTO)));
    }
    public ResponseEntity<APIResponse> submitAssess(Map<String, Object> data, String phone) {
        long patientId = Long.parseLong(data.get("patientId").toString());
        Patient patient = patientRepository.findById(patientId);
        if (patient == null) {
            return ResponseEntity.ok(new APIResponse(1, "Patient not found"));
        }
        User user = userRepository.findByPhoneAndDeletedFalse(phone);
        if (user == null) {
            return ResponseEntity.ok(new APIResponse(2, "Invalid User"));
        }
        LocalDateTime assessTime = Instant.parse(data.get("assessTime").toString()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        // 评估Assessment
        Assessment assessment = new Assessment();
        assessment.setAssessTime(assessTime);
        assessment.setPatientId(patientId);
        assessment.setAssessorId(user.getId());
        assessment.setAssessType(data.get("assessType").toString());
        assessmentRepository.save(assessment);
        // 创建原始记录AssessRecord(后面可能会有多次修改、生成新的记录覆盖这次评估)
        AssessRecord assessRecord = new AssessRecord();
        assessRecord.setIsOriginal(true);
        assessRecord.setRecordTime(LocalDateTime.now());
        assessRecord.setAssessmentId(assessment.getId());
        assessRecord.setRecorderId(user.getId());
        assessRecordRepository.save(assessRecord);
        // 记录问题和答案
        saveAssessRecordQAs(data, assessRecord, patientId);
        return ResponseEntity.ok(new APIResponse(Map.of("assessmentId", assessment.getId())));
    }
    public ResponseEntity<APIResponse> changeAssessRecord(Map<String, Object> data, String phone) {
        User user = userRepository.findByPhone(phone);
        if (user == null) {
            return ResponseEntity.ok(new APIResponse(1, "User not found"));
        }
        long recordId = Long.parseLong(data.get("recordId").toString());
        AssessRecord assessRecordOld = assessRecordRepository.findById(recordId);
        if (assessRecordOld == null) {
            return ResponseEntity.ok(new APIResponse(2, "Current AssessRecord not found"));
        }
        long assessmentId = assessRecordOld.getAssessmentId();
        Assessment assessment = assessmentRepository.findById(assessmentId);
        if (assessment == null) {
            return ResponseEntity.ok(new APIResponse(3, "Assessment not found"));
        }
        // 创建新的AssessRecord
        AssessRecord assessRecord = new AssessRecord();
        assessRecord.setIsOriginal(false);
        assessRecord.setRecordTime(LocalDateTime.now());
        assessRecord.setAssessmentId(assessmentId);
        assessRecord.setRecorderId(user.getId());
        assessRecord.setChangeReason(data.get("changeReason").toString());
        assessRecordRepository.save(assessRecord);
        // 记录问题和答案
        saveAssessRecordQAs(data, assessRecord, assessment.getPatientId());
        return ResponseEntity.ok(new APIResponse(Map.of("recordId", assessRecord.getId())));
    }

    private void saveAssessRecordQAs(Map<String, Object> data, AssessRecord assessRecord, long patientId) {
        List<Map<String, Object>> questionAnswers = (List<Map<String, Object>>) data.get("questionAnswers");
        for (Map<String, Object> questionAnswer : questionAnswers) {
            RecordQuestion recordQuestion = new RecordQuestion();
            recordQuestion.setRecordId(assessRecord.getId());
            recordQuestion.setQuestionNo(questionAnswer.get("questionNo").toString());
            recordQuestion.setQuestionContent(questionAnswer.get("questionContent").toString());
            recordQuestion.setAnswerContent(questionAnswer.get("answerContent").toString());
            recordQuestion.setAnswerJudgement(questionAnswer.get("answerJudgement").toString());
            recordQuestion.setAnswerCorrect((boolean) questionAnswer.get("answerCorrect"));
            recordQuestionRepository.save(recordQuestion);
        }
        judgeAssessFeaturesPositive(assessRecord, questionAnswers, patientId);
    }

    private void judgeAssessFeaturesPositive(AssessRecord assessRecord, List<Map<String, Object>> questionAnswers, Long patientId) {
        boolean features1Positive = false;
        boolean features2Positive = false;
        boolean features3Positive = false;
        boolean features4Positive = false;
        List<String> questionNoListFeature1 = List.of("8", "9", "10", "21", "18", "19", "20");
        List<String> questionNoListFeature2 = List.of("4", "5", "6A", "6B", "6C", "6D", "7A", "7B", "7C", "7D", "7E", "16", "17");
        List<String> questionNoListFeature3 = List.of("1", "2", "3", "13", "14", "15");
        List<String> questionNoListFeature4 = List.of("11A", "11B", "12");
        for (Map<String, Object> questionAnswer : questionAnswers) {
            String questionNo = questionAnswer.get("questionNo").toString();
            if (questionNoListFeature1.contains(questionNo)) {
                features1Positive = features1Positive || !(boolean) questionAnswer.get("answerCorrect");
            } else if (questionNoListFeature2.contains(questionNo)) {
                features2Positive = features2Positive || !(boolean) questionAnswer.get("answerCorrect");
            } else if (questionNoListFeature3.contains(questionNo)) {
                features3Positive = features3Positive || !(boolean) questionAnswer.get("answerCorrect");
            } else if (questionNoListFeature4.contains(questionNo)) {
                features4Positive = features4Positive || !(boolean) questionAnswer.get("answerCorrect");
            }
        }
        assessRecord.setFeature1Positive(features1Positive);
        assessRecord.setFeature2Positive(features2Positive);
        assessRecord.setFeature3Positive(features3Positive);
        assessRecord.setFeature4Positive(features4Positive);
        assessRecordRepository.save(assessRecord);
        // 22:检查患者之前的评估结果，较此次是否有特征改变，若有则特征1为阳性
        if (features1Positive) return;
        List<Assessment> assessments = assessmentRepository.findAllByPatientIdOrderByAssessTimeDesc(patientId);
        if (assessments.size() > 1) {
            AssessRecord lastAssessRecord = assessRecordRepository.findTopByAssessmentIdOrderByRecordTimeDesc(assessments.get(1).getId());
            if (lastAssessRecord != null) {
                boolean isChange = false;
                if (!lastAssessRecord.getFeature1Positive().equals(false)) {
                    isChange = true;
                } else if (!lastAssessRecord.getFeature2Positive().equals(features2Positive)) {
                    isChange = true;
                } else if (!lastAssessRecord.getFeature3Positive().equals(features3Positive)) {
                    isChange = true;
                } else if (!lastAssessRecord.getFeature4Positive().equals(features4Positive)) {
                    isChange = true;
                }
                if (isChange) {
                    assessRecord.setFeature1Positive(true);
                    assessRecordRepository.save(assessRecord);
                }
            }
        }
    }
}
