package com.deliverit.models.dto;

import com.deliverit.models.Address;
import com.deliverit.models.Role;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class UserTransferDataDto {

    public static final String FIRST_NAME_ERROR_MESSAGE = "First name should be between 3 and 20 characters";
    public static final String LAST_NAME_ERROR_MESSAGE = "Last name should be between 3 and 20 characters";
    public static final String EMAIL_ERROR_MESSAGE = "Email should be between 10 and 40 characters";


    private int id;

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
    private Address address;

    @NotNull
    private Set<String> roles;

    public UserTransferDataDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public boolean isEmployee() {

        return roles.stream().anyMatch(u -> u.equalsIgnoreCase("Employee"));

    }

    public boolean isCustomer() {

        return roles.stream().anyMatch(u -> u.equalsIgnoreCase("Customer"));

    }

}
