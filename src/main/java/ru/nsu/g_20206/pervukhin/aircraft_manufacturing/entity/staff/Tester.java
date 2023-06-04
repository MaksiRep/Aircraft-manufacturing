package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TESTER")
public class Tester {

    public Tester() {
    }

    public Tester(Integer testerId, Integer laboratoryId) {
        this.testerId = testerId;
        this.laboratoryId = laboratoryId;
    }

    @Id
    @Column(name = "TESTER_ID")
    private Integer testerId;

    @Column(name = "LABORATORY_ID")
    private Integer laboratoryId;

    public Integer getTesterId() {
        return testerId;
    }

    public void setTesterId(Integer testerId) {
        this.testerId = testerId;
    }

    public Integer getLaboratoryId() {
        return laboratoryId;
    }

    public void setLaboratoryId(Integer laboratoryId) {
        this.laboratoryId = laboratoryId;
    }
}
