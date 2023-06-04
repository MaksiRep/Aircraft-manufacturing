package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GLIDER")
public class Glider {

    @Id
    @Column(name = "GLIDER_ID")
    private Integer gliderId;

    @Column(name = "WINGSPAN")
    private Integer wingspan;

    public Integer getGliderId() {
        return gliderId;
    }

    public void setGliderId(Integer gliderId) {
        this.gliderId = gliderId;
    }

    public Integer getWingspan() {
        return wingspan;
    }

    public void setWingspan(Integer wingspan) {
        this.wingspan = wingspan;
    }
}
