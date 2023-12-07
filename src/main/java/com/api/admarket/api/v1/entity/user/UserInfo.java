package com.api.admarket.api.v1.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String surName;

    @Column(nullable = false, length = 50)
    private String country;

    @Column(nullable = false)
    private String city;

    @Column(name = "street_address", nullable = false)
    private String street;

    @Column(nullable = false)
    private String postalCode;

    private String state;

    @Column(columnDefinition = "TEXT")
    private String additionalInfo;

}
