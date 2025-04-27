package com.smartclassroom.occupancysystem.repositories;

import com.smartclassroom.occupancysystem.domain.BuildingBlock;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuildingBlockRepository extends JpaRepository<BuildingBlock, Long> {
    Optional<BuildingBlock> findByName(@NotBlank String blockName);

    BuildingBlock getBuildingBlockByName(@NotBlank String blockName);
}
