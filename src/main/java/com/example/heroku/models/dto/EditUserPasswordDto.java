package com.deliverit.models.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class EditUserPasswordDto {

    private static final String PASSWORD_ERROR_MESSAGE = "Password should be between 6 and 20 characters.";
    private static final String PREVIOUS_PASSWORD_ERROR_MESSAGE = "Previous password not match.";

    private int id;

    @NotEmpty
    @Size(min = 6,max = 20,message = PREVIOUS_PASSWORD_ERROR_MESSAGE)
    private String previousPassword;

    @NotEmpty
    @Size(min = 6,max = 20,message = PASSWORD_ERROR_MESSAGE)
    private String password;

    @NotEmpty
    @Size(min = 6,max = 20,message = PASSWORD_ERROR_MESSAGE)
    private String confirmPassword;

    public EditUserPasswordDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getPreviousPassword() {
        return previousPassword;
    }

    public void setPreviousPassword(String previousPassword) {
        this.previousPassword = previousPassword;
    }
}
