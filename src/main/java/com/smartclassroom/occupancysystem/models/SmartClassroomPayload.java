package com.smartclassroom.occupancysystem.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SmartClassroomPayload {
    @NotBlank
    private String classroomName;
    @NotBlank
    private Integer floorNumber;
    @NotBlank
    private String blockName;
    @NotBlank
    private Integer occupancy;
    @NotBlank
    private Integer classroomCapacity;
}
