package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ENGINEERING_STAFF")
public class EngineeringStaff {

    public EngineeringStaff() {
    }

    public EngineeringStaff(Integer engineeringId, String specialization) {
        this.engineeringId = engineeringId;
        this.specialization = specialization;
    }

    public EngineeringStaff(String specialization) {

        this.specialization = specialization;
    }

    @Id
    @Column(name = "ENGINEERING_ID")
    private Integer engineeringId;

    @Column(name = "SPECIALIZATION")
    private String specialization;

    public Integer getEngineeringId() {
        return engineeringId;
    }

    public void setEngineeringId(Integer engineeringId) {
        this.engineeringId = engineeringId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
