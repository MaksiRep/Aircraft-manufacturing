package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "HELICOPTER")
public class Helicopter {

    @Id
    @Column(name = "HELICOPTER_ID")
    private Integer helicopterId;

    @Column(name = "PROPELLERS_SIZE")
    private Integer propellersSize;

    public Integer getHelicopterId() {
        return helicopterId;
    }

    public void setHelicopterId(Integer helicopterId) {
        this.helicopterId = helicopterId;
    }

    public Integer getPropellersSize() {
        return propellersSize;
    }

    public void setPropellersSize(Integer propellersSize) {
        this.propellersSize = propellersSize;
    }
}
