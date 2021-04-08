package com.deliverit.controllers.rest;


import com.deliverit.controllers.authentications.AuthenticationHelper;
import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.IllegalDeleteException;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.User;
import com.deliverit.models.Warehouse;
import com.deliverit.models.dto.WarehouseDto;
import com.deliverit.services.contracts.WarehouseService;
import com.deliverit.modelmappers.WarehouseMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {


    private final WarehouseService service;
    private final WarehouseMapper warehouseMapper;
    private final AuthenticationHelper authenticationHelper;


    @Autowired
    public WarehouseController(WarehouseService service,
                               WarehouseMapper warehouseMapper,
                               AuthenticationHelper authenticationHelper) {
        this.service = service;
        this.warehouseMapper = warehouseMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @ApiOperation(value = "Get all warehouses")
    @GetMapping
    public List<Warehouse> getAll(@RequestParam(required = false) Optional<String> address,
                                  @RequestParam(required = false) Optional<String> city,
                                  @RequestParam(required = false) Optional<String> country) {

        return service.getAll(address, city, country);

    }

    @ApiOperation(value = "Get by id")
    @GetMapping("/{id}")
    public Warehouse getById(@RequestHeader HttpHeaders headers,
                             @PathVariable int id) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            return service.getById(id, user);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @ApiOperation(value = "Create warehouse")
    @PostMapping
    public Warehouse create(@RequestHeader HttpHeaders headers,
                            @Valid @RequestBody WarehouseDto warehouseDto) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            Warehouse warehouse = warehouseMapper.fromDto(warehouseDto);

            return service.create(warehouse, user);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());

        } catch (DuplicateEntityException e) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @ApiOperation(value = "Update warehouse")
    @PutMapping("/{id}")
    public Warehouse update(@RequestHeader HttpHeaders headers,
                            @PathVariable int id,
                            @Valid @RequestBody WarehouseDto warehouseDto) {

        User user = authenticationHelper.tryGetUser(headers);

        try {
            Warehouse warehouse = warehouseMapper.fromDto(id, warehouseDto);

            return service.update(warehouse, user);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @ApiOperation(value = "Delete warehouse")
    @DeleteMapping("/{id}")
    public Warehouse delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            return service.delete(id, user);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());

        } catch (IllegalDeleteException e) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
