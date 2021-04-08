package com.deliverit.models.dto;

import javax.validation.constraints.NotNull;

public class WarehouseDto {

    public static final String ADDRESS_ERROR_MESSAGE = "Address cannot be null.";
    @NotNull(message = ADDRESS_ERROR_MESSAGE)
    private AddressDto address;


    public WarehouseDto() {
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }
}
