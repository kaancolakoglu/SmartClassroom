package com.smartclassroom.occupancysystem.controllers;

import com.smartclassroom.occupancysystem.models.ClassroomSummaryDTO;
import com.smartclassroom.occupancysystem.services.ClassroomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ClassroomController {

    private final ClassroomService classroomService;

    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @GetMapping("/classrooms")
    public ResponseEntity<List<ClassroomSummaryDTO>> getAllClassrooms() {
        return ResponseEntity.ok(classroomService.getAllClassrooms());
    }

    @GetMapping("/classrooms/building/{buildingName}")
    public ResponseEntity<List<ClassroomSummaryDTO>> getClassroomsByBuildingName(@PathVariable String buildingName) {
        return classroomService.getClassroomsByBuildingName(buildingName)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
