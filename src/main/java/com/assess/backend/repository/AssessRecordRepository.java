package com.assess.backend.repository;

import com.assess.backend.entity.AssessRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessRecordRepository extends JpaRepository<AssessRecord, Long> {
    AssessRecord findById(long id);
    Void deleteById(long id);
    AssessRecord save(AssessRecord assessRecord);
    List<AssessRecord> findAllByAssessmentId(long assessmentId);
    AssessRecord findTopByAssessmentIdOrderByRecordTimeDesc(long assessmentId);
}
