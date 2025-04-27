package com.smartclassroom.occupancysystem.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "classroom")
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String classroomName;
    @NotBlank
    private Integer floorNumber;
    @NotBlank
    private Integer occupancy;
    @NotBlank
    private Integer classroomCapacity;

    @ManyToOne
    @JoinColumn(name = "buildingBlockId")
    private BuildingBlock buildingBlock;

}
