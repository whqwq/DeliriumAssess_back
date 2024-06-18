package com.assess.backend.service;

import lombok.Data;

import java.util.Map;

@Data
public class APIResponse {
    private Integer status;
    private Map<String, Object> data;
    private String message;
    public APIResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
    public APIResponse(Map<String, Object> data) {
        this.status = 0;
        this.data = data;
    }
    public APIResponse(Map<String, Object> data, String message) {
        this.status = 0;
        this.data = data;
        this.message = message;
    }
    public APIResponse() {
        this.status = 0;
    }
}
