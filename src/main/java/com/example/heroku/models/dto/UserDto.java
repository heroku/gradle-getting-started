package com.deliverit.models.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class UserDto {

    public static final String FIRST_NAME_ERROR_MESSAGE = "First name should be between 3 and 20 characters";
    public static final String LAST_NAME_ERROR_MESSAGE = "Last name should be between 3 and 20 characters";
    public static final String EMAIL_ERROR_MESSAGE = "Email should be between 10 and 40 characters";

    @NotNull
    @Size(min = 3, max = 20, message = FIRST_NAME_ERROR_MESSAGE)
    private String firstName;

    @NotNull
    @Size(min = 3, max = 20, message = LAST_NAME_ERROR_MESSAGE)
    private String lastName;

    @NotNull
    @Size(min = 10, max = 40, message = EMAIL_ERROR_MESSAGE)
    private String email;


    @NotNull
    private AddressDto address;

    public UserDto() {
    }

    public UserDto(AddressDto address) {
        this.address = address;
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

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }
}
