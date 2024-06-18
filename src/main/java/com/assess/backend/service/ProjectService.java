package com.assess.backend.service;

import com.assess.backend.entity.DoctorProject;
import com.assess.backend.entity.Project;
import com.assess.backend.repository.AdministratorRepository;
import com.assess.backend.repository.DoctorProjectRepository;
import com.assess.backend.repository.ProjectRepository;
import com.assess.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final DoctorProjectRepository doctorProjectRepository;
    private final UserRepository userRepository;
    private final AdministratorRepository administratorRepository;
    @Autowired
    public ProjectService(ProjectRepository projectRepository, DoctorProjectRepository doctorProjectRepository, UserRepository userRepository, AdministratorRepository administratorRepository) {
        this.projectRepository = projectRepository;
        this.doctorProjectRepository = doctorProjectRepository;
        this.userRepository = userRepository;
        this.administratorRepository = administratorRepository;
    }

    @RequestMapping("/getAllProjects")
    public ResponseEntity<APIResponse> getAllProjects(String phone) {
        List<Project> projects = new ArrayList<>();
        if (userRepository.findByPhone(phone) == null && administratorRepository.findByPhone(phone) == null) {
            return ResponseEntity.ok(new APIResponse(1, "Invalid phone number"));
        }
        if (administratorRepository.findByPhone(phone) != null) {
            projects = projectRepository.findAllByDeletedFalse();
        }
        else {
            List<DoctorProject> doctorProjects = doctorProjectRepository.findAllByDoctorPhone(phone);
            for (DoctorProject doctorProject : doctorProjects) {
                Project p = projectRepository.findByProjectId(doctorProject.getProjectId());
                if (!p.getDeleted()) {
                    projects.add(p);
                }
            }
        }
        Map<String, Object> responseDate = Map.of("projects", projects);
        return ResponseEntity.ok(new APIResponse(responseDate));
    }
    public ResponseEntity<APIResponse> deleteProject(Map<String, Object> data) {
        String projectId = data.get("projectId").toString();
        Project project = projectRepository.findByProjectId(projectId);
        if (project == null) {
            return ResponseEntity.ok(new APIResponse(1, "Project not found"));
        }
        project.setDeleted(true);
        projectRepository.save(project);
        return ResponseEntity.ok(new APIResponse());
    }
    public ResponseEntity<APIResponse> changeProjectInfo(Map<String, Object> data) {
        String projectId = data.get("projectId").toString();
        Project project = projectRepository.findByProjectId(projectId);
        if (project == null) {
            return ResponseEntity.ok(new APIResponse(1, "Project not found"));
        }
        project.setProjectName(data.get("projectName").toString());
        project.setDescription(data.get("description").toString());
        projectRepository.save(project);
        return ResponseEntity.ok(new APIResponse());
    }
    public ResponseEntity<APIResponse> createProject(Map<String, Object> data) {
        String projectId = data.get("projectId").toString();
        if (projectRepository.findByProjectId(projectId) != null) {
            return ResponseEntity.ok(new APIResponse(1, "Project already exists"));
        }
        String projectName = data.get("projectName").toString();
        String description = data.get("description").toString();
        Project project = new Project();
        project.setProjectId(projectId);
        project.setProjectName(projectName);
        project.setDescription(description);
        projectRepository.save(project);
        List<Map<String, Object>> leaders = (List<Map<String, Object>>) data.get("leaders");
        for (Map<String, Object> leader : leaders) {
            DoctorProject doctorProject = new DoctorProject();
            doctorProject.setProjectId(projectId);
            doctorProject.setDoctorPhone(leader.get("phone").toString());
            doctorProject.setIsLeader(true);
            doctorProject.setHospitalIdInProject(leader.get("hospitalIdInProject").toString());
            doctorProject.setHospitalNameInProject(leader.get("hospitalNameInProject").toString());
            doctorProjectRepository.save(doctorProject);
        }
        return ResponseEntity.ok(new APIResponse());
    }
}
