package com.deliverit.repositories.contracts;

import com.deliverit.models.Address;

import java.util.List;

public interface AddressRepository {
    Address getById(int id);

    Address create(Address address);

    Address getByAddressAndCity(String name, int cityId);

    List<Address> getAll();
}
