package com.deliverit.controllers.rest;

import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.City;
import com.deliverit.services.contracts.CityService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService service;

    @Autowired
    public CityController(CityService service) {

        this.service = service;

    }


    @ApiOperation(value = "Get all cities")
    @GetMapping
    public List<City> getAll() {

        return service.getAll();

    }

    @ApiOperation(value = "Get city by id")
    @GetMapping("/{id}")
    public City getById(@PathVariable int id) {

        try {

            return service.getById(id);

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        }

    }


}
