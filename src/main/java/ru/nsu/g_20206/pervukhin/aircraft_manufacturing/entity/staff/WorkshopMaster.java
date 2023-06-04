package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff;

import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.entity_id.WorkshopMasterId;

import javax.persistence.*;

@Entity
@IdClass(WorkshopMasterId.class)
@Table(name = "WORKSHOP_MASTER")
public class WorkshopMaster {

    public WorkshopMaster() {
    }

    public WorkshopMaster(Integer workshopId, Integer engineerId) {
        this.workshopId = workshopId;
        this.engineerId = engineerId;
    }

    @Id
    @Column(name = "WORKSHOP_ID")
    private Integer workshopId;

    @Id
    @Column(name = "ENGINEER_ID")
    private Integer engineerId;

    public Integer getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(Integer workshopId) {
        this.workshopId = workshopId;
    }

    public Integer getEngineerId() {
        return engineerId;
    }

    public void setEngineerId(Integer engineerId) {
        this.engineerId = engineerId;
    }
}
