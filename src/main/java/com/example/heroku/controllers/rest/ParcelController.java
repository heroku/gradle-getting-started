package com.deliverit.controllers.rest;


import com.deliverit.controllers.authentications.AuthenticationHelper;
import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.IllegalDeleteException;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.Parcel;
import com.deliverit.models.User;
import com.deliverit.models.dto.ParcelDto;
import com.deliverit.services.contracts.ParcelService;
import com.deliverit.modelmappers.ParcelMapper;
import io.swagger.annotations.ApiOperation;
import jdk.jfr.BooleanFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/parcels")
public class ParcelController {

    private final ParcelService service;
    private final ParcelMapper parcelMapper;
    private final AuthenticationHelper authenticationHelper;


    @Autowired
    public ParcelController(ParcelService service, ParcelMapper parcelMapper,
                            AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.parcelMapper = parcelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @ApiOperation(value = "Get all parcels")
    @GetMapping
    public List<Parcel> getAll(@RequestHeader HttpHeaders headers) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            return service.getAll(user);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }


    @ApiOperation(value = "Get parcel by id")
    @GetMapping("/{id}")
    public Parcel getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            return service.getById(user, id);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/sort")
    public List<Parcel> sort(@RequestHeader HttpHeaders headers,
                             @RequestParam(required = false) Optional<String> weight,
                             @RequestParam(required = false) Optional<String> date) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            return service.sort(user, weight, date);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());

        } catch (IllegalArgumentException e) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(value = "Filter parcels")
    @GetMapping("/filter")
    public List<Parcel> filter(@RequestHeader HttpHeaders headers,
                               @RequestParam(required = false) Optional<Double> weight,
                               @RequestParam(required = false) Optional<String> customer,
                               @RequestParam(required = false) Optional<String> warehouse,
                               @RequestParam(required = false) Optional<String> category) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            return service.filter(user, weight, customer, warehouse, category);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @ApiOperation(value = "Create parcel")
    @PostMapping
    public Parcel create(@RequestHeader HttpHeaders headers,
                         @Valid @RequestBody ParcelDto parcelDto) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            Parcel parcel = parcelMapper.fromDto(parcelDto);

            return service.create(parcel, user);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        } catch (DuplicateEntityException e) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @ApiOperation(value = "Update parcel")
    @PutMapping("/{id}")
    public Parcel update(@RequestHeader HttpHeaders headers, @PathVariable int id,
                         @Valid @RequestBody ParcelDto parcelDto) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            Parcel parcel = parcelMapper.fromDto(id, parcelDto);

            return service.update(parcel, user);

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @ApiOperation(value = "Delete parcel")
    @DeleteMapping("/{id}")
    public Parcel delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {

        try {

            User user = authenticationHelper.tryGetUser(headers);

            return service.delete(id, user);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        } catch (IllegalDeleteException e) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
