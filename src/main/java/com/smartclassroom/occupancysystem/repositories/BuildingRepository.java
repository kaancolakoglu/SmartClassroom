package com.smartclassroom.occupancysystem.repositories;

import com.smartclassroom.occupancysystem.domain.Building;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long> {
    Optional<Building> findByName(@NotBlank String blockName);

    Building getBuildingBlockByName(@NotBlank String blockName);
}
