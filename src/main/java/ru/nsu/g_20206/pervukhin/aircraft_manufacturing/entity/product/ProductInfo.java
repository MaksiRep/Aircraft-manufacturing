package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUCT_INFO")
public class ProductInfo {

    public ProductInfo() {
    }

    @Id
    @Column(name = "PRODUCT_ID")
    private Integer productId;

    @Column(name = "WORKSHOP_ID")
    private Integer workshopId;

    @Column(name = "DEV_CYCLE_ID")
    private Integer devCycleId;

    @Column(name = "TESTING_CYCLE_ID")
    private Integer testingCycleId;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(Integer workshopId) {
        this.workshopId = workshopId;
    }

    public Integer getDevCycleId() {
        return devCycleId;
    }

    public void setDevCycleId(Integer devCycleId) {
        this.devCycleId = devCycleId;
    }

    public Integer getTestingCycleId() {
        return testingCycleId;
    }

    public void setTestingCycleId(Integer testingCycleId) {
        this.testingCycleId = testingCycleId;
    }
}
