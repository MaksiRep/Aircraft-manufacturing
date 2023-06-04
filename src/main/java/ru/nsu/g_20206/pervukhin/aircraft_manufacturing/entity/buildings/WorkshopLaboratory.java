package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings;

import ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.entity_id.WorkshopLaboratoryId;

import javax.persistence.*;

@Entity
@IdClass(WorkshopLaboratoryId.class)
@Table(name = "WORKSHOP_LABORATORY")
public class WorkshopLaboratory {

    public WorkshopLaboratory(Integer workshopId, Integer laboratoryId) {
        this.workshopId = workshopId;
        this.laboratoryId = laboratoryId;
    }

    public WorkshopLaboratory() {
    }

    @Id
    @Column(name = "WORKSHOP_ID")
    private Integer workshopId;

    @Id
    @Column(name = "LABORATORY_ID")
    private Integer laboratoryId;

    public Integer getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(Integer workshopId) {
        this.workshopId = workshopId;
    }

    public Integer getLaboratoryId() {
        return laboratoryId;
    }

    public void setLaboratoryId(Integer laboratoryId) {
        this.laboratoryId = laboratoryId;
    }
}
