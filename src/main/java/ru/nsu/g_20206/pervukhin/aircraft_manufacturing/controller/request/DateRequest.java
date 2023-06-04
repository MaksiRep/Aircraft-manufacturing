package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DateRequest {

    @JsonProperty (value = "firstDate")
    private String firstDate;

    @JsonProperty (value = "secondDate")
    private String secondDate;

    public String getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(String firstDate) {
        this.firstDate = firstDate;
    }

    public String getSecondDate() {
        return secondDate;
    }

    public void setSecondDate(String secondDate) {
        this.secondDate = secondDate;
    }
}
