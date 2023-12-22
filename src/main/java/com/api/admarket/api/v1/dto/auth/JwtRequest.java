package com.api.admarket.api.v1.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JwtRequest {

    @NotBlank(message = "Username can not be empty.")
    private String username;

    @NotBlank(message = "Password can not be empty.")
    private String password;

}
