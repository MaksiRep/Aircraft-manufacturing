package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductRequest {

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "weight")
    private Integer weight;

    @JsonProperty(value = "maxSpeed")
    private Integer maxSpeed;

    @JsonProperty(value = "maxHeight")
    private Integer maxHeight;

    @JsonProperty(value = "productType")
    private Integer productType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Integer getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Integer maxHeight) {
        this.maxHeight = maxHeight;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }
}
