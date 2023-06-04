package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.buildings;

import javax.persistence.*;

@Entity
@Table(name = "WORKSHOP")
public class Workshop {

    public Workshop() {
    }

    public Workshop(Integer id, String name, String address, Integer managerId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.managerId = managerId;
    }

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ADDRESS")
    private String address;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }
}
