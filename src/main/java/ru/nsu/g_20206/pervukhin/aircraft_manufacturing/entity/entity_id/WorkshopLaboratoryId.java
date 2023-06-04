package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.entity_id;

import java.io.Serializable;

public class WorkshopLaboratoryId implements Serializable {

    private Integer workshopId;

    private Integer laboratoryId;

    public WorkshopLaboratoryId() {
    }

    public WorkshopLaboratoryId(Integer workshopId, Integer laboratoryId) {
        this.workshopId = workshopId;
        this.laboratoryId = laboratoryId;
    }
}
