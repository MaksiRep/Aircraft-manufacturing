package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.entity_id;

import java.io.Serializable;

public class SectionMasterId implements Serializable {

    private Integer sectionId;

    private Integer engineerId;

    public SectionMasterId() {
    }

    public SectionMasterId(Integer sectionId, Integer engineerId) {
        this.sectionId = sectionId;
        this.engineerId = engineerId;
    }
}
