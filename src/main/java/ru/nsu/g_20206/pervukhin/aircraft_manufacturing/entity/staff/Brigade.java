package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BRIGADE")
public class Brigade {

    public Brigade() {
    }

    public Brigade(Integer brigadeId, String name, Integer sectionId, Integer brigadierId) {
        this.brigadeId = brigadeId;
        this.name = name;
        this.sectionId = sectionId;
        this.brigadierId = brigadierId;
    }

    @Id
    @Column(name = "BRIGADE_ID")
    private Integer brigadeId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SECTION_ID")
    private Integer sectionId;

    @Column(name = "BRIGADIER_ID")
    private Integer brigadierId;

    public Integer getBrigadeId() {
        return brigadeId;
    }

    public void setBrigadeId(Integer brigadeId) {
        this.brigadeId = brigadeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public Integer getBrigadierId() {
        return brigadierId;
    }

    public void setBrigadierId(Integer brigadierId) {
        this.brigadierId = brigadierId;
    }
}
