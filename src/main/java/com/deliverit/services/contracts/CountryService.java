package com.deliverit.services.contracts;

import com.deliverit.models.Country;

import java.util.List;

public interface CountryService {
    List<Country> getAll();

    Country getById(int id);
}
