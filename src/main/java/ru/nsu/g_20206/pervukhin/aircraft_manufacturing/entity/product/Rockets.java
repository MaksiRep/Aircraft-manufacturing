package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ROCKETS")
public class Rockets {

    @Id
    @Column(name = "ROCKETS_ID")
    private Integer rocketsId;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "CHARGE_POWER")
    private Integer chargePower;

    public Integer getRocketsId() {
        return rocketsId;
    }

    public void setRocketsId(Integer rocketsId) {
        this.rocketsId = rocketsId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getChargePower() {
        return chargePower;
    }

    public void setChargePower(Integer chargePower) {
        this.chargePower = chargePower;
    }
}
