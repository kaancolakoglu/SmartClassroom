package com.smartclassroom.occupancysystem.services;

import com.smartclassroom.occupancysystem.models.SmartClassroomPayload;

public interface MessageProcessor {
    void processMessage(SmartClassroomPayload smartClassroomPayload);
}
