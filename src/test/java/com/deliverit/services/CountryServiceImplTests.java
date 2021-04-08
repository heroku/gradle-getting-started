package com.deliverit.services;

import com.deliverit.models.City;
import com.deliverit.models.Country;
import com.deliverit.repositories.contracts.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.deliverit.Helpers.createMockCity;
import static com.deliverit.Helpers.createMockCountry;

@ExtendWith(MockitoExtension.class)
public class CountryServiceImplTests {


    @Mock
    CountryRepository countryRepository;


    @InjectMocks
    CountryServiceImpl countryService;


    @Test
    public void getAll_Should_ReturnAllCities_When_Exist() {

        List<Country> result = new ArrayList<>();

        Mockito.when(countryRepository.getAll())
                .thenReturn(result);

        countryService.getAll();

        Mockito.verify(countryRepository, Mockito.times(1)).getAll();

    }

    @Test
    public void getById_Should_ReturnCity_When_Exist() {

        Country country = createMockCountry();

        Mockito.when(countryRepository.getById(country.getId()))
                .thenReturn(country);

        countryService.getById(country.getId());

        Mockito.verify(countryRepository, Mockito.times(1))
                .getById(country.getId());

    }
}
