package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CertainProductRequest {

    public CertainProductRequest() {
    }

    @JsonProperty(value = "serialNumber")
    private Integer serialNumber;

    @JsonProperty(value = "productId")
    private Integer productId;

    @JsonProperty(value = "startDate")
    private String startDate;

    @JsonProperty(value = "endDate")
    private String endDate;

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
