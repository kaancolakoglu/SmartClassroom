package com.smartclassroom.occupancysystem.repositories;

import com.smartclassroom.occupancysystem.domain.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
}
