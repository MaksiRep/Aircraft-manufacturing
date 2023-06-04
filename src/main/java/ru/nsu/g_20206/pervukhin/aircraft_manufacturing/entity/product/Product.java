package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUCT")
public class Product {

    public Product() {
    }

    public Product(Integer id, String name, Integer weight, Integer maxSpeed, Integer maxHeight, Integer productType) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.maxSpeed = maxSpeed;
        this.maxHeight = maxHeight;
        this.productType = productType;
    }

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "WEIGHT")
    private Integer weight;

    @Column(name = "MAX_SPEED")
    private Integer maxSpeed;

    @Column(name = "MAX_HEIGHT")
    private Integer maxHeight;

    @Column(name = "PRODUCT_TYPE")
    private Integer productType;

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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Integer getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Integer maxHeight) {
        this.maxHeight = maxHeight;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }
}
