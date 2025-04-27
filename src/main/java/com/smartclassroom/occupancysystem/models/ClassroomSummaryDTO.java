package com.smartclassroom.occupancysystem.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomSummaryDTO {
    private Long id;
    private String classroomName;
    private Integer floorNumber;
    private Integer occupancy;
    private Integer classroomCapacity;

    private String buildingName;
}
