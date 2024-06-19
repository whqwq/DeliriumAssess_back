package com.assess.backend.repository;

import com.assess.backend.entity.DoctorProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorProjectRepository extends JpaRepository<DoctorProject, Long> {
    DoctorProject findById(long id);
    Void deleteById(long id);
    DoctorProject save(DoctorProject doctorProject);
    List<DoctorProject> findAllByDoctorId(long doctorId);
    List<DoctorProject> findAllByProjectId(String projectId);
    DoctorProject findByProjectIdAndDoctorId(String projectId, long doctorId);
}
