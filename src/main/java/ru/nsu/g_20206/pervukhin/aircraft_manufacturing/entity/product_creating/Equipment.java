package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EQUIPMENT")
public class Equipment {

    public Equipment() {
    }

    public Equipment(Integer id, String name, Integer laboratoryId) {
        this.id = id;
        this.name = name;
        this.laboratoryId = laboratoryId;
    }

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LABORATORY_ID")
    private Integer laboratoryId;

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

    public Integer getLaboratoryId() {
        return laboratoryId;
    }

    public void setLaboratoryId(Integer laboratoryId) {
        this.laboratoryId = laboratoryId;
    }
}
