package com.deliverit.models.dto;

import com.deliverit.models.Address;
import com.deliverit.models.City;
import com.deliverit.models.Country;
import com.deliverit.models.Role;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegisterDto {

    public static final String FIRST_LAST_NAME_ERROR_MESSAGE = "First name should be between 3 and 20 characters";
    public static final String EMAIL_ERROR_MESSAGE = "Email should be between 10 and 40 characters";
    private static final String PASSWORD_ERROR_MESSAGE = "Password should be between 6 and 20 characters.";

    @NotEmpty
    @Size(min = 3, max = 20, message = FIRST_LAST_NAME_ERROR_MESSAGE)
    private String firstName;

    @NotEmpty
    @Size(min = 3, max = 20, message = FIRST_LAST_NAME_ERROR_MESSAGE)
    private String lastName;

    @NotEmpty
    @Size(min = 10, max = 40, message = EMAIL_ERROR_MESSAGE)
    private String email;

    @NotEmpty
    @Size(min = 6,max = 20,message = PASSWORD_ERROR_MESSAGE)
    private String password;

    @NotEmpty
    @Size(min = 6,max = 20,message = PASSWORD_ERROR_MESSAGE)
    private String confirmPassword;

    @NotEmpty
    private String streetName;


    private int cityId;



    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
