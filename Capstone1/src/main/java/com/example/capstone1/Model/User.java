package com.example.capstone1.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;


@Data
@AllArgsConstructor
public class User {
    @NotEmpty(message = "Empty ID")
    private String id;
    @NotEmpty(message = "Empty username")
    @Size(min = 6,message = "Username have to be more than 5 length long")
    private String username;
    @NotEmpty(message = "Empty password")
    @Size(min = 7,message = "Password have to be more than 6 length long")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{7,}$")
    private String password;
    @NotEmpty(message = "Empty email")
    @Email(message = "Enter valid email")
    private String email ;
    @NotEmpty(message = "Empty role")
    @Pattern(regexp = "Admin|Customer",message = "Role must be Admin, or Customer")
    private String role;
    @NotNull(message = "Empty balance")
    @Positive(message = "Balance should be more than 0")
    private double balance;
    private ArrayList<Product> orders = new ArrayList<>();
}

