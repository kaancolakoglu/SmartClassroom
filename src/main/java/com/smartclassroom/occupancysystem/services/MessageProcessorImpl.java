package com.smartclassroom.occupancysystem.services;

import com.smartclassroom.occupancysystem.domain.BuildingBlock;
import com.smartclassroom.occupancysystem.domain.Classroom;
import com.smartclassroom.occupancysystem.models.SmartClassroomPayload;
import com.smartclassroom.occupancysystem.repositories.BuildingBlockRepository;
import com.smartclassroom.occupancysystem.repositories.ClassroomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageProcessorImpl implements MessageProcessor {
    private final ClassroomRepository classroomRepository;
    private final BuildingBlockRepository buildingBlockRepository;

    public MessageProcessorImpl(ClassroomRepository classroomRepository, BuildingBlockRepository buildingBlockRepository) {
        this.classroomRepository = classroomRepository;
        this.buildingBlockRepository = buildingBlockRepository;
    }

    @Override
    public void processMessage(SmartClassroomPayload smartClassroomPayload) {
        log.info("Processing message for classroom {} ", smartClassroomPayload.getClassroomName());

        BuildingBlock buildingBlock = buildingBlockRepository.findByName(smartClassroomPayload.getBlockName())
                .orElseGet(() -> {
                    BuildingBlock newBlock = BuildingBlock.builder()
                            .name(smartClassroomPayload.getBlockName())
                            .build();
                    return buildingBlockRepository.save(newBlock);
                });

        Classroom classroom = classroomRepository.findClassroomByClassroomNameAndBuildingBlock(
                        smartClassroomPayload.getClassroomName(), buildingBlock)
                .orElseGet(() -> {
                    Classroom newClassroom = Classroom.builder()
                            .classroomName(smartClassroomPayload.getClassroomName())
                            .floorNumber(smartClassroomPayload.getFloorNumber())
                            .occupancy(smartClassroomPayload.getOccupancy())
                            .classroomCapacity(smartClassroomPayload.getClassroomCapacity())
                            .buildingBlock(buildingBlockRepository.getBuildingBlockByName(smartClassroomPayload.getBlockName()))
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

