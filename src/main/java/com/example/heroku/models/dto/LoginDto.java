package com.deliverit.models.dto;

import javax.validation.constraints.NotEmpty;

public class LoginDto {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    public LoginDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
