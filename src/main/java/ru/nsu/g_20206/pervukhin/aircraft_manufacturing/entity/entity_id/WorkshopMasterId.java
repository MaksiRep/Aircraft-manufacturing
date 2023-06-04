package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.entity_id;

import java.io.Serializable;

public class WorkshopMasterId implements Serializable {

    private Integer workshopId;

    private Integer engineerId;

    public WorkshopMasterId() {
    }

    public WorkshopMasterId(Integer workshopId, Integer engineerId) {
        this.workshopId = workshopId;
        this.engineerId = engineerId;
    }
}
