package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PLANE")
public class Plane {

    @Id
    @Column(name = "PLANE_ID")
    private Integer planeId;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "ENGINE_COUNT")
    private Integer engineCount;

    @Column(name = "WINGSPAN")
    private Integer wingspan;

    public Integer getPlaneId() {
        return planeId;
    }

    public void setPlaneId(Integer planeId) {
        this.planeId = planeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getEngineCount() {
        return engineCount;
    }

    public void setEngineCount(Integer engineCount) {
        this.engineCount = engineCount;
    }

    public Integer getWingspan() {
        return wingspan;
    }

    public void setWingspan(Integer wingspan) {
        this.wingspan = wingspan;
    }
}
