package com.smartclassroom.occupancysystem.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SmartClassroomPayload {
    @NotBlank
    private String classroomName;

    @NotNull
    private Integer floorNumber;

    @NotBlank
    private String blockName;

    @NotNull
    private Integer occupancy;

    @NotNull
    private Integer classroomCapacity;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            return String.format("{\"classroomName\":\"%s\",\"floorNumber\":%d,\"blockName\":\"%s\",\"occupancy\":%d,\"classroomCapacity\":%d}",
                    classroomName, floorNumber, blockName, occupancy, classroomCapacity);
        }
    }
}
