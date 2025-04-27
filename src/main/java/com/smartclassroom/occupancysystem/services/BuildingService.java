package com.smartclassroom.occupancysystem.services;

import com.smartclassroom.occupancysystem.models.BuildingDTO;

import java.util.List;
public interface BuildingService {
    List<BuildingDTO> getAllBuildingBlocks();
}
