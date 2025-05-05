package com.smartclassroom.occupancysystem.repositories;

import com.smartclassroom.occupancysystem.domain.Building;
import com.smartclassroom.occupancysystem.domain.Classroom;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    Optional<Classroom> findClassroomByClassroomNameAndBuilding(@NotBlank String classroomName, Building building);

    List<Classroom> findAllByBuilding(Building building);
}
