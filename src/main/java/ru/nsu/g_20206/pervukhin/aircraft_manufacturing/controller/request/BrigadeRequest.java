package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BrigadeRequest {

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "sectionId")
    private Integer sectionId;

    @JsonProperty(value = "brigadierId")
    private Integer brigadierId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public Integer getBrigadierId() {
        return brigadierId;
    }

    public void setBrigadierId(Integer brigadierId) {
        this.brigadierId = brigadierId;
    }
}
