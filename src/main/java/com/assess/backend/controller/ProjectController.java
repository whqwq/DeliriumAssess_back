package com.assess.backend.controller;

import com.assess.backend.service.APIResponse;
import com.assess.backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }
    @RequestMapping("/getAllProjects")
    public ResponseEntity<APIResponse> getAllProjects(@RequestHeader("phone") String phone) {
        return projectService.getAllProjects(phone);
    }
    @RequestMapping("/getProjectInfo")
    public ResponseEntity<APIResponse> getProjectInfo(@RequestBody Map<String, Object> data) {
        return projectService.getProjectInfo(data);
    }
    @RequestMapping("/createProject")
    public ResponseEntity<APIResponse> createProject(@RequestBody Map<String, Object> data) {
        return projectService.createProject(data);
    }
    @RequestMapping("/deleteProject")
    public ResponseEntity<APIResponse> deleteProject(@RequestBody Map<String, Object> data) {
        return projectService.deleteProject(data);
    }
    @RequestMapping("/changeProjectInfo")
    public ResponseEntity<APIResponse> changeProjectInfo(@RequestBody Map<String, Object> data) {
        return projectService.changeProjectInfo(data);
    }
    @RequestMapping("/getProjectMembers")
    public ResponseEntity<APIResponse> getProjectMembers(@RequestBody Map<String, Object> data) {
        return projectService.getProjectMembers(data);
    }
    @RequestMapping("/addProjectMember")
    public ResponseEntity<APIResponse> addProjectMember(@RequestBody Map<String, Object> data) {
        return projectService.addProjectMember(data);
    }
    @RequestMapping("/deleteProjectMember")
    public ResponseEntity<APIResponse> deleteProjectMember(@RequestBody Map<String, Object> data) {
        return projectService.deleteProjectMember(data);
    }
}
