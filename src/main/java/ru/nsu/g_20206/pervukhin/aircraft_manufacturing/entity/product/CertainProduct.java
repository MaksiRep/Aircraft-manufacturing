package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table (name = "CERTAIN_PRODUCT")
public class CertainProduct {

    public CertainProduct() {
    }

    public CertainProduct(Integer serialNumber, Integer productId, Date startDate, Date endDate) {
        this.serialNumber = serialNumber;
        this.productId = productId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Id
    @Column(name = "SERIAL_NUMBER")
    private Integer serialNumber;

    @Column(name = "PRODUCT_ID")
    private Integer productId;

    @Column(name = "START_DATE")
    private Date startDate;

   @Column(name = "END_DATE")
    private Date endDate;

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
