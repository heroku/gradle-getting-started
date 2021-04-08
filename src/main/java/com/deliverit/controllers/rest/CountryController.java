package com.deliverit.controllers.rest;

import com.deliverit.services.contracts.CountryService;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Country;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {
    CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {

        this.countryService = countryService;

    }

    @ApiOperation(value = "Get all countries")
    @GetMapping
    public List<Country> getAll() {

        return countryService.getAll();

    }

    @ApiOperation(value = "Get country by id")
    @GetMapping("/{id}")
    public Country getById(@PathVariable int id) {

        try {

            return countryService.getById(id);

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        }
    }
}
