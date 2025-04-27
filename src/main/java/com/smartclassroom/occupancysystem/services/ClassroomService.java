package com.smartclassroom.occupancysystem.services;

import com.smartclassroom.occupancysystem.models.ClassroomSummaryDTO;

import java.util.List;
import java.util.Optional;

public interface ClassroomService {
    List<ClassroomSummaryDTO> getAllClassrooms();
    Optional<List<ClassroomSummaryDTO>> getClassroomsByBuildingName(String buildingName);
}
