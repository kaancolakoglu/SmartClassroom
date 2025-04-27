package com.smartclassroom.occupancysystem.controllers;

import com.smartclassroom.occupancysystem.models.BuildingDTO;
import com.smartclassroom.occupancysystem.services.BuildingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/buildings")
@Slf4j
public class BuildingController {

    private final BuildingService buildingService;

    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GetMapping
    public ResponseEntity<String> getBlockName() {
        return ResponseEntity.ok("Building Service");
    }

    @GetMapping("/all")
    public ResponseEntity<List<BuildingDTO>> getAllBuildings() {
        return ResponseEntity.ok(buildingService.getAllBuildingBlocks());
    }
}
