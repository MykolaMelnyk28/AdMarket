package com.api.admarket.api.v1.entity.user;

import jakarta.persistence.*;

@Entity
@Table(name = "user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String surName;

    @Column(nullable = false)
    private String city;

    @Column(name = "street_address", nullable = false)
    private String street;

    @Column(nullable = false)
    private String postalCode;

    private String state;

    private String additionalInfo;


}
