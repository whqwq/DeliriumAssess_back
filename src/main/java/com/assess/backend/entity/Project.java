package com.assess.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    private String projectId;
    private String projectName;
    private String description;
    private Boolean deleted;
    public String toString() {
        return "Project(projectId=" + projectId+ ", projectName=" + projectName + ", description=" + description + ")";
    }
}
