package com.assess.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String doctorPhone;
    private String projectId;
    private Boolean isLeader;
    private String hospitalNameInProject;
    private String hospitalIdInProject;
    public String toString() {
        return "DoctorProject(id=" + id+ ", doctorPhone=" + doctorPhone + ", projectId=" + projectId + ", isLeader=" + isLeader + ", hospitalNameInProject=" + hospitalNameInProject + ", hospitalIdInProject=" + hospitalIdInProject + ")";
    }
}
