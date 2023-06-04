package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TEST_CYCLE")
public class TestCycle {

    public TestCycle() {
    }

    public TestCycle(Integer cycleId) {
        this.cycleId = cycleId;
    }

    @Id
    @Column(name = "CYCLE_ID")
    private Integer cycleId;

    public Integer getCycleId() {
        return cycleId;
    }

    public void setCycleId(Integer cycleId) {
        this.cycleId = cycleId;
    }
}
