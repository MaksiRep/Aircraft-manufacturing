package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WorkerStaff {

    public WorkerStaff() {
    }

    public WorkerStaff(Integer workerId, Integer brigadeId) {
        this.workerId = workerId;
        this.brigadeId = brigadeId;
    }

    @Id
    @Column(name = "WORKER_ID")
    private Integer workerId;

    @Column(name = "BRIGADE_ID")
    private Integer brigadeId;

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public Integer getBrigadeId() {
        return brigadeId;
    }

    public void setBrigadeId(Integer brigadeId) {
        this.brigadeId = brigadeId;
    }
}
