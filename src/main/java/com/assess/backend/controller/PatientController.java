package com.assess.backend.controller;

import com.assess.backend.service.APIResponse;
import com.assess.backend.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {
    private final PatientService patientService;
    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
    @RequestMapping("/getProjectPatients")
    public ResponseEntity<APIResponse> getProjectPatients(@RequestBody Map<String, Object> data, @RequestHeader("phone") String phone) {
        return patientService.getProjectPatients(data, phone);
    }
    @RequestMapping("/addPatient")
    public ResponseEntity<APIResponse> addPatient(@RequestBody Map<String, Object> data, @RequestHeader("phone") String phone) {
        return patientService.addPatient(data, phone);
    }
    @RequestMapping("/deletePatient")
    public ResponseEntity<APIResponse> deletePatient(@RequestBody Map<String, Object> data) {
        return patientService.deletePatient(data);
    }
    @RequestMapping("/getPatientDetails")
    public ResponseEntity<APIResponse> getPatientDetails(@RequestBody Map<String, Object> data, @RequestHeader("phone") String phone) {
        return patientService.getPatientDetails(data, phone);
    }
}
