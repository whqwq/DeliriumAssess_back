package com.assess.backend.controller;

import com.assess.backend.service.APIResponse;
import com.assess.backend.service.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PublicController {
    private final PublicService publicService;
    @Autowired
    public PublicController(PublicService publicService) {
        this.publicService = publicService;
    }
    @RequestMapping("/login")
    public ResponseEntity<APIResponse> login(@RequestBody Map<String, Object> data) {
        return publicService.login(data);
    }
}
