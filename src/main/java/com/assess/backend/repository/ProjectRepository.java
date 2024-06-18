package com.assess.backend.repository;

import com.assess.backend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAll();
    List<Project> findAllByDeletedFalse();
    Project findByProjectId(String projectId);
    Void deleteByProjectId(String projectId);
    Project save(Project project);
}
