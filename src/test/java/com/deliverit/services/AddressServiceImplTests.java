package com.deliverit.services;

import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Address;
import com.deliverit.repositories.contracts.AddressRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.deliverit.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceImplTests {


    @Mock
    AddressRepository addressRepository;

    @InjectMocks
    AddressServiceImpl addressService;


    @Test
    public void getById_Should_ReturnAddress_When_MatchExist() {

        Address address = createMockAddress();

        Mockito.when(addressRepository.getById(1))
                .thenReturn(address);

        Address result = addressService.getById(1);

        Assertions.assertEquals(1, result.getId());

        Assertions.assertEquals("MockStreetName", result.getStreetName());

    }

    @Test
    public void getByAddressAndCity_Should_ReturnAddress_When_Exist() {

        Address address = createMockAddress();

        Mockito.when(addressService.getByAddressAndCity(address.getStreetName(), address.getCity().getId()))
                .thenReturn(address);


        Address result = addressService.getByAddressAndCity(address.getStreetName(), address.getCity().getId());


        Assertions.assertEquals(1, result.getId());

        Assertions.assertEquals("MockStreetName", result.getStreetName());


    }

    @Test
    public void create_Should_ThrowException_When_AddressNotFound() {

        Address address = createMockAddress();

        Mockito.when(addressRepository.getByAddressAndCity(address.getStreetName(),
                address.getCity().getId()))
                .thenThrow(EntityNotFoundException.class);

        addressService.getOrCreate(address);

        Mockito.verify(addressRepository, Mockito.times(1)).create(address);

    }
}
