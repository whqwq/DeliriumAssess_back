package com.assess.backend.DTO.GetAssess;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessRecordDTO {
    private Long recordId;
    private LocalDateTime recordTime;
    private String recorderPhone;
    private String recorderName;
    private Boolean isOriginal;
    private Boolean isLatest;
    private Boolean feature1Positive;
    private Boolean feature2Positive;
    private Boolean feature3Positive;
    private Boolean feature4Positive;
}
