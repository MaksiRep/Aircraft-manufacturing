package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.staff;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "STAFF")
public class Staff {

    public Staff() {}

    public Staff(Integer id,
                 String name,
                 String surname,
                 Date birthDate,
                 String education,
                 Date employmentDate,
                 Date dismissalDate,
                 Integer salary,
                 Integer staffType) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.education = education;
        this.employmentDate = employmentDate;
        this.dismissalDate = dismissalDate;
        this.salary = salary;
        this.staffType = staffType;
    }

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "BIRTH_DATE")
    private Date birthDate;

    @Column(name = "EDUCATION")
    private String education;

    @Column(name = "EMPLOYMENT_DATE")
    private Date employmentDate;

    @Column(name = "DISMISSAL_DATE")
    private Date dismissalDate;

    @Column(name = "SALARY")
    private Integer salary;

    @Column(name = "STAFF_TYPE")
    private Integer staffType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date date) {
        this.birthDate = date;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public Date getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(Date employmentDate) {
        this.employmentDate = employmentDate;
    }

    public Date getDismissalDate() {
        return dismissalDate;
    }

    public void setDismissalDate(Date dismissalDate) {
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
