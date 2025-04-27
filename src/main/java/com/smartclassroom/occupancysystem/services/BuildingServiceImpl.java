package com.smartclassroom.occupancysystem.services;

import com.smartclassroom.occupancysystem.mapper.MapStructMapper;
import com.smartclassroom.occupancysystem.models.BuildingDTO;
import com.smartclassroom.occupancysystem.repositories.BuildingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BuildingServiceImpl implements BuildingService {

    private final BuildingRepository buildingRepository;
    private final MapStructMapper mapstructMapper;

    public BuildingServiceImpl(BuildingRepository buildingRepository,
                               MapStructMapper mapstructMapper) {
        this.buildingRepository = buildingRepository;
        this.mapstructMapper = mapstructMapper;
    }

    @Override
    public List<BuildingDTO> getAllBuildingBlocks() {
        log.info("Fetching all building blocks");
        return buildingRepository.findAll().stream().map(mapstructMapper::toDTO).collect(Collectors.toList());
    }
}
