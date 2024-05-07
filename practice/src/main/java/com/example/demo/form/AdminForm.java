package com.example.demo.form;

import java.io.Serializable;
import com.example.demo.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
@FieldMatch(first = "password", second = "passwordConfirmation", message = "パスワードが確認用と一致しません。")
public class AdminForm implements Serializable {

    @NotBlank
    private String lastName;

    @NotBlank
    private String firstName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirmation;
}
