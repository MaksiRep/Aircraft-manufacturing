package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.entity_id;

import java.io.Serializable;

public class TestingCycleId implements Serializable {

    private Integer cycleId;

    private Integer ordNum;

    public TestingCycleId() {
    }

    public TestingCycleId(Integer cycleId, Integer ordNum) {
        this.cycleId = cycleId;
        this.ordNum = ordNum;
    }
}
