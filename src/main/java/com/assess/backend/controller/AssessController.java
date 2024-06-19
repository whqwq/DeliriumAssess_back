package com.assess.backend.controller;

import com.assess.backend.service.APIResponse;
import com.assess.backend.service.AssessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/assess")
public class AssessController {
    private final AssessService assessService;
    @Autowired
    public AssessController(AssessService assessService) {
        this.assessService = assessService;
    }
    @RequestMapping("/getAssess")
    public ResponseEntity<APIResponse> getAssess(@RequestBody Map<String, Object> data, @RequestHeader("phone") String phone) {
        return assessService.getAssess(data, phone);
    }
    @RequestMapping("/getAssessRecordDetails")
    public ResponseEntity<APIResponse> getAssessRecordDetails(@RequestBody Map<String, Object> data, @RequestHeader("phone") String phone) {
        return assessService.getAssessRecordDetails(data, phone);
    }
    @RequestMapping("/submitAssess")
    public ResponseEntity<APIResponse> submitAssess(@RequestBody Map<String, Object> data, @RequestHeader("phone") String phone) {
        return assessService.submitAssess(data, phone);
    }
    @RequestMapping("/changeAssessRecord")
    public ResponseEntity<APIResponse> changeAssessRecord(@RequestBody Map<String, Object> data, @RequestHeader("phone") String phone) {
        return assessService.changeAssessRecord(data, phone);
    }
}
