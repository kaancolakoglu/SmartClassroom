package com.smartclassroom.occupancysystem.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomDTO {
    private Long id;
    private String classroomName;
    private Integer floorNumber;
    private Integer occupancy;
    private Integer classroomCapacity;
    private BuildingDTO building;
}
