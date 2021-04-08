package com.deliverit.models.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

public class ShipmentDto {


    public static final String STATUS_ID_ERROR_MESSAGE = "Status ID must be positive.";

    @NotNull
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date departureDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date arrivalDate;

    @Positive(message = STATUS_ID_ERROR_MESSAGE)
    private int statusId;


    public ShipmentDto() {
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

}
