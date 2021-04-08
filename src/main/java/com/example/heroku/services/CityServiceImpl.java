package com.deliverit.services;

import com.deliverit.models.City;
import com.deliverit.repositories.contracts.CityRepository;
import com.deliverit.services.contracts.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(CityRepository repository) {
        this.cityRepository = repository;
    }


    public List<City> getAll() {

        return cityRepository.getAll();

    }


    public City getById(int id) {

        return cityRepository.getById(id);

    }


}
