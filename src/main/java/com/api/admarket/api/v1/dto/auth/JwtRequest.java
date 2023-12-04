package com.api.admarket.api.v1.dto.auth;

import lombok.Data;

@Data
public class JwtRequest {

    private String username;
    private String password;

}
