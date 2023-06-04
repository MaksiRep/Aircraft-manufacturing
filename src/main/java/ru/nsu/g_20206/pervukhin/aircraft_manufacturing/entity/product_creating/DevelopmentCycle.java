package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating;

import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.entity_id.DevelopmentCycleId;

import javax.persistence.*;

@Entity
@IdClass(DevelopmentCycleId.class)
@Table(name = "DEVELOPMENT_CYCLE")
public class DevelopmentCycle {

    public DevelopmentCycle() {
    }

    public DevelopmentCycle(Integer cycleId, Integer ordNum, Integer sectionId) {
        this.cycleId = cycleId;
        this.ordNum = ordNum;
        this.sectionId = sectionId;
    }

    @Id
    @Column(name = "CYCLE_ID")
    private Integer cycleId;

    @Id
    @Column(name = "ORD_NUM")
    private Integer ordNum;

    @Column(name = "SECTION_ID")
    private Integer sectionId;

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

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }
}
