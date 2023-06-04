package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUCT_TYPE")
public class ProductType {

    public ProductType() {
    }

    public ProductType(Integer typeId, String name) {
        this.typeId = typeId;
        this.name = name;
    }

    @Id
    @Column(name = "TYPE_ID")
    private Integer typeId;

    @Column(name = "NAME")
    private String name;

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
