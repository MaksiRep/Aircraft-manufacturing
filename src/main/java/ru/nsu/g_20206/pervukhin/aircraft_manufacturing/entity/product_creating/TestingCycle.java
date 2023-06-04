package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating;

import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.entity_id.TestingCycleId;

import javax.persistence.*;

@Entity
@IdClass(TestingCycleId.class)
@Table(name = "TESTING_CYCLE")
public class TestingCycle {

    public TestingCycle() {
    }

    public TestingCycle(Integer cycleId, Integer ordNum, Integer equipmentId) {
        this.cycleId = cycleId;
        this.ordNum = ordNum;
        this.equipmentId = equipmentId;
    }

    @Id
    @Column(name = "CYCLE_ID")
    private Integer cycleId;

    @Id
    @Column(name = "ORD_NUM")
    private Integer ordNum;

    @Column(name = "EQUIPMENT_ID")
    private Integer equipmentId;

    public Integer getCycleId() {
        return cycleId;
    }

    public void setCycleId(Integer cycleId) {
        this.cycleId = cycleId;
    }

    public Integer getOrdNum() {
        return ordNum;
    }

    public void setOrdNum(Integer ordNum) {
        this.ordNum = ordNum;
    }

    public Integer getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }
}
