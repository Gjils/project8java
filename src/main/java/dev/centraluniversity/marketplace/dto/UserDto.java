package dev.centraluniversity.marketplace.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema
public class UserDto {
    @NotNull
    @Size(max=255)
    private String name;

    @NotNull
    @Size(max=255)
    @Email
    private String email;

    private String address;

    @Size(max=20)
    private String phone;
}
