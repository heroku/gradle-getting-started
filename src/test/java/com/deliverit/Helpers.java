package com.deliverit;

import com.deliverit.models.*;

import java.sql.Timestamp;
import java.util.Set;

public class Helpers {


    public static User createMockUser() {
        User user = new User();

        user.setId(1);
        user.setFirstName("MockFirstName");
        user.setLastName("MockLastName");
        user.setEmail("MockEmail");
        user.setAddress(createMockAddress());
        user.setRoles(Set.of(new Role(1, "Customer")));
        return user;
    }

    public static Country createMockCountry() {
        Country country = new Country();
        country.setId(1);
        country.setName("MockCountry");
        return country;
    }

    public static City createMockCity() {
        City city = new City();
        city.setId(1);
        city.setName("MockCity");
        city.setCountry(createMockCountry());
        return city;
    }

    public static Address createMockAddress() {
        Address address = new Address();
        address.setId(1);
        address.setStreetName("MockStreetName");
        address.setCity(createMockCity());
        return address;
    }

    public static Warehouse createMockWarehouse() {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1);
        warehouse.setAddress(createMockAddress());

        return warehouse;
    }

    public static Shipment createMockShipment() {


        Shipment shipment = new Shipment();
        shipment.setId(1);
        shipment.setDepartureDate(Timestamp.valueOf("2021-03-03 01:01:01"));
        shipment.setArrivalDate(Timestamp.valueOf("2022-03-03 01:01:01"));
        shipment.setStatus(new Status(1, "on the way"));

        return shipment;
    }

    public static Parcel createMockParcel() {

        Parcel parcel = new Parcel();
        parcel.setId(1);
        parcel.setWeight(25);
        parcel.setCategory(new Category(1, "cloth"));
        parcel.setUser(createMockUser());
        parcel.setWarehouse(createMockWarehouse());

        return parcel;
    }

}
