package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SECTION")
public class Section {

    public Section() {
    }

    public Section(Integer id, String name, Integer workshopId, Integer managerId) {
        this.id = id;
        this.name = name;
        this.workshopId = workshopId;
        this.managerId = managerId;
    }

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "WORKSHOP_ID")
    private Integer workshopId;

    @Column(name = "MANAGER_ID")
    private Integer managerId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(Integer workshopId) {
        this.workshopId = workshopId;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }
}
