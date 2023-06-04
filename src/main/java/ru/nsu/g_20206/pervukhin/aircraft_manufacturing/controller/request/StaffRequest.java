package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StaffRequest {

    public StaffRequest() {
    }

    public StaffRequest(String name, String surname, String birthDate, String education, String employmentDate, String dismissalDate, Integer salary, Integer staffType) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.education = education;
        this.employmentDate = employmentDate;
        this.dismissalDate = dismissalDate;
        this.salary = salary;
        this.staffType = staffType;
    }

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "surname")
    private String surname;

    @JsonProperty(value = "birthDate")
    private String birthDate;

    @JsonProperty(value = "education")
    private String education;

    @JsonProperty(value = "employmentDate")
    private String employmentDate;

    @JsonProperty(value = "dismissalDate")
    private String dismissalDate;

    @JsonProperty(value = "salary")
    private Integer salary;

    @JsonProperty(value = "staffType")
    private Integer staffType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(String employmentDate) {
        this.employmentDate = employmentDate;
    }

    public String getDismissalDate() {
        return dismissalDate;
    }

    public void setDismissalDate(String dismissalDate) {
        this.dismissalDate = dismissalDate;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Integer getStaffType() {
        return staffType;
    }

    public void setStaffType(Integer staffType) {
        this.staffType = staffType;
    }
}
