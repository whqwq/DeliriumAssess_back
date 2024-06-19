package com.assess.backend.DTO.GetAssess;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessDTO {
    private LocalDateTime assessTime;
    private String assessType;
    private Long patientId;
    private char patientAlpha;
    private String patientIdInProject;
    private String assessorName;
    private String assessorPhone;
}
