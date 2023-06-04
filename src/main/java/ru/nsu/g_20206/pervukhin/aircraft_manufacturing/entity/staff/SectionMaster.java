package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff;

import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.entity_id.SectionMasterId;

import javax.persistence.*;

@Entity
@IdClass(SectionMasterId.class)
@Table(name = "WORKSHOP_MASTER")
public class SectionMaster {

    public SectionMaster() {
    }

    public SectionMaster(Integer sectionId, Integer engineerId) {
        this.sectionId = sectionId;
        this.engineerId = engineerId;
    }

    @Id
    @Column(name = "SECTION_ID")
    private Integer sectionId;

    @Id
    @Column(name = "ENGINEER_ID")
    private Integer engineerId;

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public Integer getEngineerId() {
        return engineerId;
    }

    public void setEngineerId(Integer engineerId) {
        this.engineerId = engineerId;
    }
}
