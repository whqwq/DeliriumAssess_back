package com.assess.backend.DTO.GetAssessRecordDetails;

import com.assess.backend.entity.RecordQuestion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordDetailsDTO {
    private Long recordId;
    private LocalDateTime recordTime;
    private String assessType;
    private Long patientId;
    private String patientIdInProject;
    private char patientAlpha;
    private String recorderPhone;
    private String recorderName;
    private String changeReason;
    private Boolean isLatest;
    private List<RecordQuestion> recordQuestions;
}
