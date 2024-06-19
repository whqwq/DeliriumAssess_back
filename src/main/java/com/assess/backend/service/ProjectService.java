package com.assess.backend.service;

import com.assess.backend.DTO.GetProject.GetProjectDTO;
import com.assess.backend.entity.DoctorProject;
import com.assess.backend.entity.Project;
import com.assess.backend.entity.User;
import com.assess.backend.repository.AdministratorRepository;
import com.assess.backend.repository.DoctorProjectRepository;
import com.assess.backend.repository.ProjectRepository;
import com.assess.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public ResponseEntity<APIResponse> getAllProjects(String phone) {
        List<GetProjectDTO> projectsDTO = new ArrayList<>();
        if (userRepository.findByPhoneAndDeletedFalse(phone) == null && administratorRepository.findByPhone(phone) == null) {
            return ResponseEntity.ok(new APIResponse(1, "Invalid phone number"));
        }
        if (administratorRepository.findByPhone(phone) != null) {
            List<Project> projects = projectRepository.findAllByDeletedFalse();
            for (Project project : projects) {
                GetProjectDTO p = new GetProjectDTO();
                p.setProjectId(project.getProjectId());
                p.setProjectName(project.getProjectName());
                p.setDescription(project.getDescription());
                p.setIsLeader(true);
                projectsDTO.add(p);
            }
        }
        else {
            long userId = userRepository.findByPhoneAndDeletedFalse(phone).getId();
            List<DoctorProject> doctorProjects = doctorProjectRepository.findAllByDoctorId(userId);
            for (DoctorProject doctorProject : doctorProjects) {
                Project project = projectRepository.findByProjectIdAndDeletedFalse(doctorProject.getProjectId());
                if (!project.getDeleted()) {
                    GetProjectDTO p = new GetProjectDTO();
                    p.setProjectId(project.getProjectId());
                    p.setProjectName(project.getProjectName());
                    p.setDescription(project.getDescription());
                    p.setIsLeader(doctorProject.getIsLeader());
                    projectsDTO.add(p);
                }
            }
        }
        Map<String, Object> responseDate = Map.of("projects", projectsDTO);
        return ResponseEntity.ok(new APIResponse(responseDate));
    }
    public ResponseEntity<APIResponse> getProjectInfo(Map<String, Object> data) {
        String projectId = data.get("projectId").toString();
        Project project = projectRepository.findByProjectIdAndDeletedFalse(projectId);
        if (project == null) {
            return ResponseEntity.ok(new APIResponse(1, "Project not found"));
        }
        Map<String, Object> responseData = Map.of("project", project);
        return ResponseEntity.ok(new APIResponse(responseData));
    }
    public ResponseEntity<APIResponse> deleteProject(Map<String, Object> data) {
        String projectId = data.get("projectId").toString();
        Project project = projectRepository.findByProjectIdAndDeletedFalse(projectId);
        if (project == null) {
            return ResponseEntity.ok(new APIResponse(1, "Project not found"));
        }
        project.setDeleted(true);
        projectRepository.save(project);
        return ResponseEntity.ok(new APIResponse());
    }
    public ResponseEntity<APIResponse> changeProjectInfo(Map<String, Object> data) {
        String projectId = data.get("projectId").toString();
        Project project = projectRepository.findByProjectIdAndDeletedFalse(projectId);
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
            // todo deleted project can be created again
            return ResponseEntity.ok(new APIResponse(1, "ProjectID already exists"));
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
            doctorProject.setDoctorId(Long.parseLong(leader.get("id").toString()));
            doctorProject.setIsLeader(true);
            doctorProject.setHospitalIdInProject(leader.get("hospitalIdInProject").toString());
            doctorProject.setHospitalNameInProject(leader.get("hospitalNameInProject").toString());
            doctorProjectRepository.save(doctorProject);
        }
        return ResponseEntity.ok(new APIResponse());
    }
    public ResponseEntity<APIResponse> getProjectMembers(Map<String, Object> data) {
        String projectId = data.get("projectId").toString();
        Project project = projectRepository.findByProjectIdAndDeletedFalse(projectId);
        if (project == null) {
            return ResponseEntity.ok(new APIResponse(1, "Project not found"));
        }
        List<DoctorProject> doctorProjects = doctorProjectRepository.findAllByProjectId(project.getProjectId());
        List<Map<String, Object>> members = new ArrayList<>();
        for (DoctorProject doctorProject : doctorProjects) {
            if (userRepository.findById(doctorProject.getDoctorId()).isEmpty()) {
                continue;
            }
            User user = userRepository.findById(doctorProject.getDoctorId()).get();
            Map<String, Object> member = Map.of(
                    "id", doctorProject.getDoctorId(),
                    "phone", user.getPhone(),
                    "name", user.getName(),
                    "hospitalIdInProject", doctorProject.getHospitalIdInProject(),
                    "hospitalNameInProject", doctorProject.getHospitalNameInProject(),
                    "isLeader", doctorProject.getIsLeader()
            );
            members.add(member);
        }
        Map<String, Object> responseData = Map.of("members", members);
        return ResponseEntity.ok(new APIResponse(responseData));
    }
    public ResponseEntity<APIResponse> addProjectMember(Map<String, Object> data) {
        String projectId = data.get("projectId").toString();
        Project project = projectRepository.findByProjectIdAndDeletedFalse(projectId);
        if (project == null) {
            return ResponseEntity.ok(new APIResponse(1, "Project not found"));
        }
        String phone = data.get("phone").toString();
        User user = userRepository.findByPhoneAndDeletedFalse(phone);
        if (user == null) {
            return ResponseEntity.ok(new APIResponse(2, "User not found"));
        }
        if (doctorProjectRepository.findByProjectIdAndDoctorId(projectId, user.getId()) != null) {
            return ResponseEntity.ok(new APIResponse(3, "User already in project"));
        }
        DoctorProject doctorProject = new DoctorProject();
        doctorProject.setProjectId(projectId);
        doctorProject.setDoctorId(user.getId());
        doctorProject.setHospitalIdInProject(data.get("hospitalIdInProject").toString());
        doctorProject.setHospitalNameInProject(data.get("hospitalNameInProject").toString());
        doctorProject.setIsLeader(false);
        doctorProjectRepository.save(doctorProject);
        return ResponseEntity.ok(new APIResponse());
    }
    public ResponseEntity<APIResponse> deleteProjectMember(Map<String, Object> data) {
        String projectId = data.get("projectId").toString();
        long doctorId = Long.parseLong(data.get("doctorId").toString());
        Project project = projectRepository.findByProjectIdAndDeletedFalse(projectId);
        if (project == null) {
            return ResponseEntity.ok(new APIResponse(1, "Project not found"));
        }
        if (userRepository.findById(doctorId).isEmpty()) {
            return ResponseEntity.ok(new APIResponse(2, "Doctor not found"));
        }
        DoctorProject doctorProject = doctorProjectRepository.findByProjectIdAndDoctorId(projectId, doctorId);
        if (doctorProject == null) {
            return ResponseEntity.ok(new APIResponse(3, "Doctor not in project"));
        }
        doctorProjectRepository.delete(doctorProject);
        return ResponseEntity.ok(new APIResponse());
    }
}
