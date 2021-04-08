package com.deliverit.models.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class AddressDto {

    public static final String ADDRESS_ERROR_MESSAGE = "Address must be between 5 and 50 characters.";
    public static final String CITY_ID_ERROR_MESSAGE = "City ID must be positive.";

    @NotNull
    @Size(min = 5,max = 50,message = ADDRESS_ERROR_MESSAGE)
    private String streetName;

    @Positive(message = CITY_ID_ERROR_MESSAGE)
    private int cityId;

    public AddressDto() {
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
