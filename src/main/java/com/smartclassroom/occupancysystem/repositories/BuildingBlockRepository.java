package com.smartclassroom.occupancysystem.repositories;

import com.smartclassroom.occupancysystem.domain.BuildingBlock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingBlockRepository extends JpaRepository<BuildingBlock, Long> {
}
