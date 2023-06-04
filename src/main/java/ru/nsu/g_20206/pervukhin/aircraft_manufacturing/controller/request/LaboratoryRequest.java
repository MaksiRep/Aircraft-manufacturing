package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LaboratoryRequest {

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "address")
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
