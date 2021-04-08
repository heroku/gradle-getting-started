package com.deliverit.services;


import com.deliverit.models.City;
import com.deliverit.repositories.contracts.CityRepository;
import com.deliverit.services.contracts.CityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.deliverit.Helpers.createMockCity;

@ExtendWith(MockitoExtension.class)
public class CityServiceImplTests {

    @Mock
    CityRepository cityRepository;


    @InjectMocks
    CityServiceImpl cityService;


    @Test
    public void getAll_Should_ReturnAllCities_When_Exist() {

        List<City> result = new ArrayList<>();

        Mockito.when(cityRepository.getAll())
                .thenReturn(result);

        cityService.getAll();

        Mockito.verify(cityRepository, Mockito.times(1)).getAll();

    }

    @Test
    public void getById_Should_ReturnCity_When_Exist() {

        City city = createMockCity();

        Mockito.when(cityRepository.getById(city.getId()))
                .thenReturn(city);

        cityService.getById(city.getId());

        Mockito.verify(cityRepository, Mockito.times(1)).getById(city.getId());

    }


}
