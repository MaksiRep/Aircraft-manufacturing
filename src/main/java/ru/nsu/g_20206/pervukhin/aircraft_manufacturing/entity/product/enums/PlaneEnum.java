package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.enums;

public enum PlaneEnum {

    CIVILIAN("Гражданский"),
    TRANSPORT("Транспортный"),
    MILITARY("Военный");

    private final String value;

    PlaneEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
