package com.deliverit.services;

import com.deliverit.services.contracts.CountryService;
import com.deliverit.models.Country;
import com.deliverit.repositories.contracts.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public List<Country> getAll() {

        return countryRepository.getAll();

    }

    @Override
    public Country getById(int id) {

        return countryRepository.getById(id);

    }
}
