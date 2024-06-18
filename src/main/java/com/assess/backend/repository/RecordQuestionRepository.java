package com.assess.backend.repository;

import com.assess.backend.entity.RecordQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordQuestionRepository extends JpaRepository<RecordQuestion, Long>{
    RecordQuestion findById(long id);
    Void deleteById(long id);
    RecordQuestion save(RecordQuestion recordQuestion);
    List<RecordQuestion> findAllByRecordId(long recordId);
}
