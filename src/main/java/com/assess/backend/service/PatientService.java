package com.assess.backend.service;

import com.assess.backend.DTO.GetPatientDetails.PatientAssessDTO;
import com.assess.backend.entity.*;
import com.assess.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PatientService {
    private final PatientRepository patientRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final DoctorProjectRepository doctorProjectRepository;
    private final AssessmentRepository assessmentRepository;
    @Autowired
    public PatientService(PatientRepository patientRepository, ProjectRepository projectRepository, UserRepository userRepository, DoctorProjectRepository doctorProjectRepository, AssessmentRepository assessmentRepository) {
        this.patientRepository = patientRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.doctorProjectRepository = doctorProjectRepository;
        this.assessmentRepository = assessmentRepository;
    }
    public ResponseEntity<APIResponse> getProjectPatients(Map<String, Object> data, String phone) {
        String projectId = data.get("projectId").toString();
        if (projectId == null) {
            return ResponseEntity.ok(new APIResponse(1, "projectId is required"));
        }
        User user = userRepository.findByPhoneAndDeletedFalse(phone);
        if (user == null) {
            return ResponseEntity.ok(new APIResponse(2, "Invalid User"));
        }
        Project project = projectRepository.findByProjectIdAndDeletedFalse(projectId);
        if (project == null) {
            return ResponseEntity.ok(new APIResponse(3, "Invalid projectId"));
        }
        DoctorProject doctorProject = doctorProjectRepository.findByProjectIdAndDoctorId(projectId, user.getId());
        if (doctorProject == null) {
            return ResponseEntity.ok(new APIResponse(4, "User not in this project"));
        }
        Map<String, Object> responseData = Map.of("patients", patientRepository.findAllByProjectIdAndDeletedFalse(projectId));
        return ResponseEntity.ok(new APIResponse(responseData));
    }

    public ResponseEntity<APIResponse> addPatient(Map<String, Object> data, String phone) {
        String projectId = data.get("projectId").toString();
        Project project = projectRepository.findByProjectIdAndDeletedFalse(projectId);
        if (project == null) {
            return ResponseEntity.ok(new APIResponse(1, "Invalid projectId"));
        }
        User user = userRepository.findByPhoneAndDeletedFalse(phone);
        if (user == null) {
            return ResponseEntity.ok(new APIResponse(2, "Invalid User"));
        }
        DoctorProject doctorProject = doctorProjectRepository.findByProjectIdAndDoctorId(projectId, user.getId());
        if (doctorProject == null) {
            return ResponseEntity.ok(new APIResponse(3, "User not in this project"));
        }
        if (patientRepository.findByProjectIdAndPatientIdInProjectAndDeletedFalse(projectId, data.get("patientIdInProject").toString()) != null) {
            return ResponseEntity.ok(new APIResponse(4, "Patient ID already exists in this project"));
        }
        Patient patient = new Patient();
        patient.setProjectId(projectId);
        patient.setAlpha(data.get("alpha").toString().charAt(0));
        patient.setPatientIdInProject(data.get("patientIdInProject").toString());
        String dateTimeStr = data.get("operateDate").toString();
        patient.setOperateDate(Instant.parse(dateTimeStr).atZone(ZoneId.systemDefault()).toLocalDate());
        patient.setHospitalIdInProject(doctorProject.getHospitalIdInProject());
        patient.setHospitalNameInProject(doctorProject.getHospitalNameInProject());
        patientRepository.save(patient);
        return ResponseEntity.ok(new APIResponse());
    }

    public ResponseEntity<APIResponse> deletePatient(Map<String, Object> data) {
        long id = Long.parseLong(data.get("id").toString());
        Patient patient = patientRepository.findByIdAndDeletedFalse(id);
        if (patient == null) {
            return ResponseEntity.ok(new APIResponse(1, "Invalid patientId"));
        }
        patient.setDeleted(true);
        patientRepository.save(patient);
        return ResponseEntity.ok(new APIResponse());
    }

    public ResponseEntity<APIResponse> getPatientDetails(Map<String, Object> data, String phone) {
        long id = Long.parseLong(data.get("id").toString());
        Patient patient = patientRepository.findByIdAndDeletedFalse(id);
        if (patient == null) {
            return ResponseEntity.ok(new APIResponse(1, "Invalid patientId"));
        }
        User user = userRepository.findByPhoneAndDeletedFalse(phone);
        if (user == null) {
            return ResponseEntity.ok(new APIResponse(2, "Invalid User"));
        }
        DoctorProject doctorProject = doctorProjectRepository.findByProjectIdAndDoctorId(patient.getProjectId(), user.getId());
        if (doctorProject == null) {
            return ResponseEntity.ok(new APIResponse(3, "User not in this project"));
        }
        List<Assessment> assessments = assessmentRepository.findAllByPatientId(patient.getId());
        // 创建一个DTO来传输格式化后的数据
        List<PatientAssessDTO> patientAssessDTOs = new ArrayList<>();
        for (Assessment assessment : assessments) {
            long assessorId = assessment.getAssessorId();
            User assessor = userRepository.findById(assessorId);
            if (assessor == null) {
                return ResponseEntity.ok(new APIResponse(4, "Invalid assessorId"));
            }
            PatientAssessDTO patientAssessDTO = new PatientAssessDTO(assessment.getId(), assessment.getAssessTime(), assessment.getAssessType(), assessment.getAssessorId(), assessor.getName(), assessor.getPhone());
            patientAssessDTOs.add(patientAssessDTO);
        }
        Map<String, Object> responseData = Map.of("patient", patient, "assessments", patientAssessDTOs);
        return ResponseEntity.ok(new APIResponse(responseData));
    }
}
