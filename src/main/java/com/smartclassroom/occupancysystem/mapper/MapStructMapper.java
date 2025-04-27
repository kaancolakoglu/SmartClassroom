package com.smartclassroom.occupancysystem.mapper;

import com.smartclassroom.occupancysystem.domain.Building;
import com.smartclassroom.occupancysystem.domain.Classroom;
import com.smartclassroom.occupancysystem.models.BuildingDTO;
import com.smartclassroom.occupancysystem.models.ClassroomDTO;
import com.smartclassroom.occupancysystem.models.ClassroomSummaryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
    BuildingDTO toDTO(Building building);

    ClassroomDTO toDto(Classroom classroom);

    @Mapping(target = "buildingName", source = "building.name")
    ClassroomSummaryDTO toDTO(Classroom classroom);
}
