package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request;


import com.fasterxml.jackson.annotation.JsonProperty;

public class DevelopmentRequest {

    public DevelopmentRequest() {
    }

    @JsonProperty(value = "serialNumber")
    private Integer serialNumber;

    @JsonProperty(value = "cycleId")
    private Integer cycleId;

    @JsonProperty(value = "devStep")
    private Integer devStep;

    @JsonProperty(value = "startDevDate")
    private String startDevDate;

    @JsonProperty(value = "endDevDate")
    private String endDevDate;

    @JsonProperty(value = "brigadeId")
    private Integer brigadeId;

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

    public String getStartDevDate() {
        return startDevDate;
    }

    public void setStartDevDate(String startDevDate) {
        this.startDevDate = startDevDate;
    }

    public String getEndDevDate() {
        return endDevDate;
    }

    public void setEndDevDate(String endDevDate) {
        this.endDevDate = endDevDate;
    }

    public Integer getBrigadeId() {
        return brigadeId;
    }

    public void setBrigadeId(Integer brigadeId) {
        this.brigadeId = brigadeId;
    }
}
