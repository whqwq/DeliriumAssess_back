package com.assess.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private char alpha;
    private String projectId;
    private LocalDate operateDate;
    private String patientIdInProject;
    private String hospitalNameInProject;
    private String hospitalIdInProject;
    private Boolean deleted;
    public String toString() {
        return "Patient(id=" + id+ ", alpha=" + alpha + ", projectId=" + projectId + ", operateDate=" + operateDate + ", patientIdInProject=" + patientIdInProject + ", hospitalNameInProject=" + hospitalNameInProject + ", hospitalIdInProject=" + hospitalIdInProject + ")";
    }
}
