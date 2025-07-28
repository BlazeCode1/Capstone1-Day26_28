package org.example.capstone1day26.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor

public class User {
    @NotEmpty(message = "UserID Cannot Be Empty")
    private String ID;
    @NotEmpty(message = "Username Cannot Be Empty")
    private String username;
    @NotEmpty(message = "User Password Cannot Be Empty")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,}$",message = "Password Must contain characters and digits, and a length of 6")
    private String password;

    @NotEmpty(message = "User Email Cannot Be Empty")
    @Email(message = "Invalid Email Format")
    private String email;

    @NotEmpty(message = "User Role Cannot Be Empty")
    @Pattern(regexp = "^(admin|customer)$",message = "Invalid Customer Role")
    private String role;


    @NotNull(message = "balance Cannot Be Empty")
    @Positive(message = "Only Positive numbers")
    private double balance;
}
