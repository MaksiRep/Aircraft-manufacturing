package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product_creating;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "PRODUCT_TESTING")
public class ProductTesting {

    public ProductTesting() {
    }

    public ProductTesting(Integer id,
                          Integer serialNumber,
                          Integer cycleId,
                          Integer testStep,
                          Integer testerId,
                          Date startTestDate,
                          Date endTestDate) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.cycleId = cycleId;
        this.testStep = testStep;
        this.testerId = testerId;
        this.startTestDate = startTestDate;
        this.endTestDate = endTestDate;
    }

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "SERIAL_NUMBER")
    private Integer serialNumber;

    @Column(name = "CYCLE_ID")
    private Integer cycleId;

    @Column(name = "TEST_STEP")
    private Integer testStep;

    @Column(name = "TESTER_ID")
    private Integer testerId;

    @Column(name = "START_TEST_DATE")
    private Date startTestDate;

    @Column(name = "END_TEST_DATE")
    private Date endTestDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getTestStep() {
        return testStep;
    }

    public void setTestStep(Integer testStep) {
        this.testStep = testStep;
    }

    public Integer getTesterId() {
        return testerId;
    }

    public void setTesterId(Integer testerId) {
        this.testerId = testerId;
    }

    public Date getStartTestDate() {
        return startTestDate;
    }

    public void setStartTestDate(Date startTestDate) {
        this.startTestDate = startTestDate;
    }

    public Date getEndTestDate() {
        return endTestDate;
    }

    public void setEndTestDate(Date endTestDate) {
        this.endTestDate = endTestDate;
    }
}
