package com.api.admarket.api.v1.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDTO {

    private Long id;
    private String username;
    private String password;
    private String passwordConfirm;
    private String email;
    private String phoneNumber;
    private String accountStatus;
    private LocalDateTime dateRegistered;
    private LocalDateTime dateUpdated;
    private String firstName;
    private String lastName;
    private String surName;
    private String city;
    private String street;
    private String postalCode;
    private String state;
    private String additionalAddressInfo;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> images;

}
