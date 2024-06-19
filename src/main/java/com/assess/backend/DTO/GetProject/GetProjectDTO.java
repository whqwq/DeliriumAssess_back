package com.assess.backend.DTO.GetProject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetProjectDTO {
    String projectId;
    String projectName;
    String description;
    Boolean isLeader = false;
}
