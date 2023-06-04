package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "HANG_GLIDER")
public class HangGlider {

    @Id
    @Column(name = "HANG_GLIDER_ID")
    private Integer hangGliderId;

    @Column(name = "WINGSPAN")
    private Integer wingspan;

    @Column(name = "CAPACITY")
    private Integer capacity;

    public Integer getHangGliderId() {
        return hangGliderId;
    }

    public void setHangGliderId(Integer hangGliderId) {
        this.hangGliderId = hangGliderId;
    }

    public Integer getWingspan() {
        return wingspan;
    }

    public void setWingspan(Integer wingspan) {
        this.wingspan = wingspan;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
