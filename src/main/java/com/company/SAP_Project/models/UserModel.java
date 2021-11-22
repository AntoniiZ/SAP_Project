package com.company.SAP_Project.models;

import com.company.SAP_Project.annotations.PasswordMatches;

import javax.validation.constraints.*;

@PasswordMatches
public class UserModel {

    @Email(message = "You must enter a valid email!", regexp = "(^\\S+@\\S+$)")
    private String email;

    @Size(min=3, max=15, message = "Your username must be between 3 and 15 chars!")
    private String username;

    @Size(min=6, message = "Your password must have more than 6 chars!")
    private String password;

    private String matchingPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}