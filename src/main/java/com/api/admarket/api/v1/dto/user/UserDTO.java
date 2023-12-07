package com.api.admarket.api.v1.dto.user;

import com.api.admarket.api.v1.dto.validation.OnCreate;
import com.api.admarket.api.v1.dto.validation.OnUpdate;
import com.api.admarket.api.v1.dto.validation.PasswordConfirm;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@PasswordConfirm(groups = OnCreate.class)
public class UserDTO {

    @Null(message = "Id can not be set", groups = {OnCreate.class, OnUpdate.class})
    @Min(value = 1, message = "Id can not be negative.", groups = {OnCreate.class, OnUpdate.class})
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Username can not be empty.",
            groups = OnCreate.class)
    @Length(max = 20,
            message = "Username must be 20 characters or less.",
            groups = OnCreate.class)
    private String username;

    @NotBlank(message = "Password can not be empty.", groups = OnCreate.class)
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!?@#$%^&*(),\\.<>\\[\\]{}\"'|\\\\:;`~+\\-*\\/]).{8,}$",
            message = "Invalid password format.",
            groups = {OnCreate.class, OnUpdate.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = "Password confirm can not be null.",
            groups = OnCreate.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordConfirm;

    @NotBlank(message = "Email can not be null.",
            groups = OnCreate.class)
    @Email(message = "Incorrect email.",
            groups = OnCreate.class)
    @Length(max = 50,
            message = "Email must be 50 characters or less.",
            groups = {OnCreate.class, OnUpdate.class})
    private String email;

    @NotBlank(message = "Phone number can not be null.",
            groups = OnCreate.class)
    @Pattern(
            regexp = "(\\+\\d{1,4}[-.\\s]?)(\\(\\d{1,}\\)[-\\s]?|\\d{1,}[-.\\s]?){1,}[0-9\\s]",
            message = "Incorrect phone number."
    )
    @Length(max = 15,
            message = "Incorrect phone number.",
            groups = {OnCreate.class, OnUpdate.class})
    private String phoneNumber;

    @NotBlank(message = "Account status can not be null.",
            groups = OnCreate.class)
    @Pattern(regexp = "^(active|noactivate|blocked)$",
            message = "Incorrect account status value.",
            groups = {OnCreate.class, OnUpdate.class})
    private String accountStatus = "noactivate";

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateRegistered;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateUpdated;

    @NotBlank(message = "Firstname can not be null.",
            groups = OnCreate.class)
    @Length(max = 20,
            message = "Firstname must be 20 characters or less.",
            groups = {OnCreate.class, OnUpdate.class})
    private String firstName;

    @NotBlank(message = "Lastname can not be null.",
            groups = OnCreate.class)
    @Length(max = 20,
            message = "Lastname must be 20 characters or less.",
            groups = {OnCreate.class, OnUpdate.class})
    private String lastName;

    @Size(max = 20,
            message = "Surname must be 20 characters or less.",
            groups = {OnCreate.class, OnUpdate.class})
    private String surName;

    @NotBlank(message = "Country can not be null or empty.",
            groups = {OnCreate.class})
    @Length(max = 50,
            message = "Country must be 50 or less")
    private String country;

    @NotBlank(message = "City can not be null.",
            groups = OnCreate.class)
    @Length(max = 50,
            message = "City must be 50 characters or less.",
            groups = {OnCreate.class, OnUpdate.class})
    private String city;

    @NotBlank(message = "Street can not be null.",
            groups = OnCreate.class)
    @Length(max = 60,
            message = "Street must be 60 characters or less.",
            groups = {OnCreate.class, OnUpdate.class})
    private String street;

    @NotBlank(message = "Postal code can not be null.",
            groups = OnCreate.class)
    @Length(max = 10,
            message = "Postal code must be 10 characters or less.",
            groups = {OnCreate.class, OnUpdate.class})
    private String postalCode;

    @Size(max = 50,
            message = "State must be 50 characters or less.",
            groups = {OnCreate.class, OnUpdate.class})
    private String state;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String role;

    private String additionalAddressInfo;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> images = new ArrayList<>();

}
