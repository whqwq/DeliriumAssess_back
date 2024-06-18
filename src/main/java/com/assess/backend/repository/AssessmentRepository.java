package com.assess.backend.repository;

import com.assess.backend.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    Assessment findById(long id);
    Void deleteById(long id);
    Assessment save(Assessment assessment);
    List<Assessment> findAllByPatientId(long patientId);
}
