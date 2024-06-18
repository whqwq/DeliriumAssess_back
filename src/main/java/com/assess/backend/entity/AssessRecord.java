package com.assess.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessRecord {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private Long assessmentId;
    private LocalDateTime recordTime;
    private String recorderPhone;
    private Boolean isOrigin;
    private String changeReason;
    private Boolean feature1Positive;
    private Boolean feature2Positive;
    private Boolean feature3Positive;
    private Boolean feature4Positive;
    public String toString() {
        return "AssessRecord(id=" + id+ ", assessmentId=" + assessmentId + ", recordTime=" + recordTime + ", recorderPhone=" + recorderPhone + ", isOrigin=" + isOrigin + ", changeReason=" + changeReason + ", feature1Positive=" + feature1Positive + ", feature2Positive=" + feature2Positive + ", feature3Positive=" + feature3Positive + ", feature4Positive=" + feature4Positive + ")";
    }
}
