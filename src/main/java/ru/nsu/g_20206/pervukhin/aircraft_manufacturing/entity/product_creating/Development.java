package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "DEVELOPMENT")
public class Development {

    public Development() {
    }

    public Development(Integer developmentId,
                       Integer serialNumber,
                       Integer cycleId,
                       Integer devStep,
                       Date startDevDate,
                       Date endDevDate,
                       Integer brigadeId) {
        this.developmentId = developmentId;
        this.serialNumber = serialNumber;
        this.cycleId = cycleId;
        this.devStep = devStep;
        this.startDevDate = startDevDate;
        this.endDevDate = endDevDate;
        this.brigadeId = brigadeId;
    }

    @Id
    @Column(name = "DEVELOPMENT_ID")
    private Integer developmentId;

    @Column(name = "SERIAL_NUMBER")
    private Integer serialNumber;

    @Column(name = "CYCLE_ID")
    private Integer cycleId;

    @Column(name = "DEV_STEP")
    private Integer devStep;

    @Column(name = "START_DEV_DATE")
    private Date startDevDate;

    @Column(name = "END_DEV_DATE")
    private Date endDevDate;

    @Column(name = "BRIGADE_ID")
    private Integer brigadeId;

    public Integer getDevelopmentId() {
        return developmentId;
    }

    public void setDevelopmentId(Integer developmentId) {
        this.developmentId = developmentId;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getCycleId() {
        return cycleId;
    }

    public void setCycleId(Integer cycleId) {
        this.cycleId = cycleId;
    }

    public Integer getDevStep() {
        return devStep;
    }

    public void setDevStep(Integer devStep) {
        this.devStep = devStep;
    }

    public Date getStartDevDate() {
        return startDevDate;
    }

    public void setStartDevDate(Date startDevDate) {
        this.startDevDate = startDevDate;
    }

    public Date getEndDevDate() {
        return endDevDate;
    }

    public void setEndDevDate(Date endDevDate) {
        this.endDevDate = endDevDate;
    }

    public Integer getBrigadeId() {
        return brigadeId;
    }

    public void setBrigadeId(Integer brigadeId) {
        this.brigadeId = brigadeId;
    }
}
