package com.assess.backend.service;

import com.assess.backend.entity.*;
import com.assess.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Service
public class AssessService {
    private final AssessmentRepository assessmentRepository;
    private final AssessRecordRepository assessRecordRepository;
    private final RecordQuestionRepository recordQuestionRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    @Autowired
    public AssessService(AssessmentRepository assessmentRepository, AssessRecordRepository assessRecordRepository, RecordQuestionRepository recordQuestionRepository, PatientRepository patientRepository, UserRepository userRepository) {
        this.assessmentRepository = assessmentRepository;
        this.assessRecordRepository = assessRecordRepository;
        this.recordQuestionRepository = recordQuestionRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }
    public ResponseEntity<APIResponse> getAssess(Map<String, Object> data, String phone) {
        return null;
    }
    public ResponseEntity<APIResponse> getAssessRecordDetails(Map<String, Object> data) {
        return null;
    }
    public ResponseEntity<APIResponse> submitAssess(Map<String, Object> data, String phone) {
        long patientId = (long) data.get("patientId");
        if (patientRepository.findById(patientId).isEmpty()) {
            return ResponseEntity.ok(new APIResponse(1, "Patient not found"));
        }
        User user = userRepository.findByPhoneAndDeletedFalse(phone);
        if (user == null) {
            return ResponseEntity.ok(new APIResponse(2, "Invalid User"));
        }
        LocalDateTime assessTime = Instant.parse((String) data.get("assessTime")).atZone(ZoneId.systemDefault()).toLocalDateTime();
        // 评估Assessment
        Assessment assessment = new Assessment();
        assessment.setAssessTime(assessTime);
        assessment.setPatientId(patientId);
        assessment.setAssessorId(user.getId());
        assessment.setScale((String) data.get("scale"));
        assessmentRepository.save(assessment);
        // 创建原始记录AssessRecord(后面可能会有多次修改、生成新的记录覆盖这次评估)
        AssessRecord assessRecord = new AssessRecord();
        assessRecord.setIsOrigin(true);
        assessRecord.setRecordTime(LocalDateTime.now());
        assessRecord.setAssessmentId(assessment.getId());
        assessRecord.setRecorderId(user.getId());
        assessRecordRepository.save(assessRecord);
        List<Map<String, Object>> questionAnswers = (List<Map<String, Object>>) data.get("questionAnswers");
        for (Map<String, Object> questionAnswer : questionAnswers) {
            RecordQuestion recordQuestion = new RecordQuestion();
            recordQuestion.setRecordId(assessRecord.getId());
            recordQuestion.setQuestionNo((String) questionAnswer.get("questionNo"));
            recordQuestion.setQuestionContent((String) questionAnswer.get("questionContent"));
            recordQuestion.setAnswerContent((String) questionAnswer.get("answerContent"));
            recordQuestion.setAnswerJudgement((String) questionAnswer.get("answerJudgement"));
            recordQuestion.setAnswerCorrect((boolean) questionAnswer.get("answerCorrect"));
            recordQuestionRepository.save(recordQuestion);
        }
        judgeAssessFeaturesPositive(assessRecord, questionAnswers, patientId);
        return ResponseEntity.ok(new APIResponse(Map.of("assessmentId", assessment.getId())));
    }
    public ResponseEntity<APIResponse> changeAssessRecord(Map<String, Object> data) {
        return null;
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
            String questionNo = (String) questionAnswer.get("questionNo");
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
