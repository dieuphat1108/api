package com.project.api.dto;

import lombok.Data;

import javax.persistence.Column;
@Data
public class RegisterDto {
    private Long id;
    private String email;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String phone;
    private String address;
    private String role;
}
