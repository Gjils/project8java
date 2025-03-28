package dev.centraluniversity.marketplace.models;

public enum OrderStatus {
    NEW("новый"),
    PROCESSING("в обработке"),
    COMPLETED("выполнен");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
