package com.lib.delivery.entity.enums;

public enum OrderStatus {
    PENDING(1, "Pending"),
    PROCESSING(2, "Processing"),
    COMPLETED(3, "Completed"),
    CANCELLED(4, "Cancelled");

    private final int id;
    private final String value;
    private final String name;

    OrderStatus(int id, String name) {
        this.id = id;
        this.value = name().toLowerCase();
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}