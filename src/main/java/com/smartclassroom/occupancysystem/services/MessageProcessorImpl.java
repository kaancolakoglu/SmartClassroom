package com.smartclassroom.occupancysystem.services;

import com.smartclassroom.occupancysystem.domain.Building;
import com.smartclassroom.occupancysystem.domain.Classroom;
import com.smartclassroom.occupancysystem.models.SmartClassroomPayload;
import com.smartclassroom.occupancysystem.repositories.BuildingRepository;
import com.smartclassroom.occupancysystem.repositories.ClassroomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageProcessorImpl implements MessageProcessor {
    private final ClassroomRepository classroomRepository;
    private final BuildingRepository buildingRepository;

    public MessageProcessorImpl(ClassroomRepository classroomRepository, BuildingRepository buildingRepository) {
        this.classroomRepository = classroomRepository;
        this.buildingRepository = buildingRepository;
    }

    @Override
    public void processMessage(SmartClassroomPayload smartClassroomPayload) {
        log.info("Processing message for classroom {} ", smartClassroomPayload.getClassroomName());

        Building building = buildingRepository.findByName(smartClassroomPayload.getBlockName())
                .orElseGet(() -> {
                    Building newBlock = Building.builder()
                            .name(smartClassroomPayload.getBlockName())
                            .build();
                    return buildingRepository.save(newBlock);
                });

        Classroom classroom = classroomRepository.findClassroomByClassroomNameAndBuilding(
                        smartClassroomPayload.getClassroomName(), building)
                .orElseGet(() -> {
                    Classroom newClassroom = Classroom.builder()
                            .classroomName(smartClassroomPayload.getClassroomName())
                            .floorNumber(smartClassroomPayload.getFloorNumber())
                            .occupancy(smartClassroomPayload.getOccupancy())
                            .classroomCapacity(smartClassroomPayload.getClassroomCapacity())
                            .building(buildingRepository.getBuildingBlockByName(smartClassroomPayload.getBlockName()))
                            .build();
                    return classroomRepository.save(newClassroom);
                });
        if (classroom.getId() != null) {
            classroom.setOccupancy(smartClassroomPayload.getOccupancy());
            classroomRepository.save(classroom);
            log.info("Updated occupancy for classroom {} to {}",
                    smartClassroomPayload.getClassroomName(), smartClassroomPayload.getOccupancy());
        }
    }
}

