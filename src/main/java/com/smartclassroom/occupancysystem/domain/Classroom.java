package com.smartclassroom.occupancysystem.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private Integer floorNumber;
    @NotNull
    private Integer occupancy;
    @NotNull
    private Integer classroomCapacity;

    @ManyToOne
    @JoinColumn(name = "buildingBlockId")
    private Building building;

}
