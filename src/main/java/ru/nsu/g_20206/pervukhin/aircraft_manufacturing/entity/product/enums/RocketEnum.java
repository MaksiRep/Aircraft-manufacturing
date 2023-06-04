package ru.nsu.g_20206.pervukhin.aircraft_manufacturing.entity.product.enums;

public enum RocketEnum {

    ARTILLERY("Артиллерийская"),
    AVIATION("Авиационная"),
    NAVAL("Военно-морская");

    private final String value;

    RocketEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
