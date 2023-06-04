package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductTestingRequest {

    public ProductTestingRequest() {
    }

    @JsonProperty(value = "serialNumber")
    private Integer serialNumber;

    @JsonProperty(value = "cycleId")
    private Integer cycleId;

    @JsonProperty(value = "testStep")
    private Integer testStep;

    @JsonProperty(value = "startTestDate")
    private String startTestDate;

    @JsonProperty(value = "endTestDate")
    private String endTestDate;

    @JsonProperty(value = "testerId")
    private Integer testerId;

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

    public String getStartTestDate() {
        return startTestDate;
    }

    public void setStartTestDate(String startTestDate) {
        this.startTestDate = startTestDate;
    }

    public String getEndTestDate() {
        return endTestDate;
    }

    public void setEndTestDate(String endTestDate) {
        this.endTestDate = endTestDate;
    }

    public Integer getTesterId() {
        return testerId;
    }

    public void setTesterId(Integer testerId) {
        this.testerId = testerId;
    }
}
