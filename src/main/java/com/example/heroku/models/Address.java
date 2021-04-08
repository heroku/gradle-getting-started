package com.deliverit.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private int id;

    @Column(name = "address")
    private String streetName;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    public Address() {
    }


    public Address(String streetName, City city) {
        this.streetName = streetName;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String address) {
        this.streetName = address;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address1 = (Address) o;
        return getStreetName().equals(address1.getStreetName()) && getCity().equals(address1.getCity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStreetName(), getCity());
    }
}
