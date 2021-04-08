package com.deliverit.services.contracts;

import com.deliverit.models.Address;

import java.util.List;

public interface AddressService {
    Address getById(int id);

    Address getOrCreate(Address address);

    Address getByAddressAndCity(String address, int cityId);

    List<Address> getAll();
}
