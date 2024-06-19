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
public class Assessment {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private Long patientId;
    private LocalDateTime assessTime;
    private String scale;
    private Long assessorId;
    public String toString() {
        return "Assessment(id=" + id+ ", patientId=" + patientId + ", assessTime=" + assessTime + ", scale=" + scale + ", assessorId=" + assessorId + ")";
    }
}
