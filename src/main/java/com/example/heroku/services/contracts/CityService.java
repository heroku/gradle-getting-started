package com.deliverit.services.contracts;

import com.deliverit.models.City;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface CityService {

    List<City> getAll();

    City getById(int id);

}
