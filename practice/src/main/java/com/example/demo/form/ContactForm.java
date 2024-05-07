package com.example.demo.form;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class ContactForm implements Serializable {

    private Long id;

	@NotBlank
    @Size(max = 255)
    private String lastName;

    @NotBlank
    @Size(max = 255)
    private String firstName;

    @NotBlank
    @Size(max = 255)
    @Email
    private String email;

    @NotBlank
    @Size(min = 10, max = 11)
    private String phone;

    @NotBlank
    @Pattern(regexp = "[0-9]{3}-?[0-9]{4}")
    private String zipCode;

    @NotBlank
    @Size(max = 255)
    private String address;

    @NotBlank
    @Size(max = 255)
    private String buildingName;

    @NotEmpty
    private String contactType;

    @NotBlank
    private String body;


    private LocalDateTime createdAt;


    private LocalDateTime  updatedAt;
}
