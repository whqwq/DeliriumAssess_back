package com.assess.backend.repository;

import com.assess.backend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findAll();
    List<Patient> findAllByDeletedFalse();
    Patient findById(long id);
    Void deleteById(long id);
    Patient save(Patient patient);
    List<Patient> findAllByProjectId(String projectId);
}
