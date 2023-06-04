package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StaffTypeRequest {

    @JsonProperty(value = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
