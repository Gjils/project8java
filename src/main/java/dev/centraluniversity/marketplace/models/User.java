package dev.centraluniversity.marketplace.models;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phone;
    private List<Order> orders = new ArrayList<>();
}