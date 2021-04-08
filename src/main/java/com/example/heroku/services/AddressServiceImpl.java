package com.deliverit.services;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Address;
import com.deliverit.repositories.contracts.AddressRepository;
import com.deliverit.services.contracts.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {


    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address getById(int id) {

        return addressRepository.getById(id);

    }

    @Override
    public Address getOrCreate(Address address) {

        try {

            return addressRepository.getByAddressAndCity(address.getStreetName(), address.getCity().getId());

        } catch (EntityNotFoundException e) {

            return addressRepository.create(address);

        }
    }

    @Override
    public Address getByAddressAndCity(String address, int cityId) {

        return addressRepository.getByAddressAndCity(address, cityId);

    }

    @Override
    public List<Address> getAll() {
        return addressRepository.getAll();
    }

}
