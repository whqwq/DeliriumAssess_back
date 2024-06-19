package com.assess.backend.DTO.GetPatientDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientAssessDTO {
    private Long id;
    private LocalDateTime assessTime;
    private String scale;
    private Long assessorId;
    private String assessorName;
    private String assessorPhone;
}
