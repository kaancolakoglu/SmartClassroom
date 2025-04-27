package com.smartclassroom.occupancysystem.services;

import com.smartclassroom.occupancysystem.domain.Building;
import com.smartclassroom.occupancysystem.mapper.MapStructMapper;
import com.smartclassroom.occupancysystem.models.ClassroomSummaryDTO;
import com.smartclassroom.occupancysystem.repositories.BuildingRepository;
import com.smartclassroom.occupancysystem.repositories.ClassroomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClassroomServiceImpl implements ClassroomService {
    private final MapStructMapper mapStructMapper;

    private final ClassroomRepository classroomRepository;
    private final BuildingRepository buildingRepository;

    public ClassroomServiceImpl(ClassroomRepository classroomRepository, BuildingRepository buildingRepository,
                                MapStructMapper mapStructMapper) {
        this.classroomRepository = classroomRepository;
        this.buildingRepository = buildingRepository;
        this.mapStructMapper = mapStructMapper;
    }

    @Override
    public List<ClassroomSummaryDTO> getAllClassrooms() {
        log.info("Fetching all classrooms");
        return classroomRepository.findAll().stream().map(mapStructMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<List<ClassroomSummaryDTO>> getClassroomsByBuildingName(String buildingName) {
        log.info("Fetching classrooms for building: {}", buildingName);
        Optional<Building> building = buildingRepository.findByName(buildingName);

        return building.map(value -> classroomRepository.findAllByBuilding(value).stream().map(mapStructMapper::toDTO).collect(Collectors.toList()));

    }
}
