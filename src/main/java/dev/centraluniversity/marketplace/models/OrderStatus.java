package dev.centraluniversity.marketplace.models;

import lombok.Getter;

@Getter
public enum OrderStatus {
    NEW("новый"),
    PROCESSING("в обработке"),
    COMPLETED("выполнен");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

}
