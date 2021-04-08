package com.deliverit.controllers.rest;

import com.deliverit.controllers.authentications.AuthenticationHelper;
import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.IllegalCreateException;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.User;
import com.deliverit.models.dto.ShipmentDto;
import com.deliverit.services.contracts.ShipmentService;
import com.deliverit.models.Shipment;
import com.deliverit.modelmappers.ShipmentMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final ShipmentMapper shipmentMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public ShipmentController(ShipmentService shipmentService,
                              ShipmentMapper shipmentMapper,
                              AuthenticationHelper authenticationHelper) {
        this.shipmentService = shipmentService;
        this.shipmentMapper = shipmentMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @ApiOperation(value = "Get all shipments")
    @GetMapping
    public List<Shipment> getAll(@RequestHeader HttpHeaders headers) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            return shipmentService.getAll(user);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @ApiOperation(value = "Get shipment by id")
    @GetMapping("/{id}")
    public Shipment getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            return shipmentService.getById(id, user);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @ApiOperation(value = "Add parcel to shipment")
    @PutMapping("/{shipmentId}/parcel")
    public void addParcelToShipment(@RequestHeader HttpHeaders headers,
                                    @PathVariable int shipmentId,
                                    @RequestBody Map<String, Integer> parcelId) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            shipmentService.addParcelToShipment(user, shipmentId, parcelId.get("parcelId"));

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        } catch (DuplicateEntityException e) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @ApiOperation(value = "Remove parcel from shipment")
    @PutMapping("/{shipmentId}/parcel/{parcelId}")
    public void removeParcelFromShipment(@RequestHeader HttpHeaders headers,
                                         @PathVariable int shipmentId,
                                         @PathVariable int parcelId) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            shipmentService.removeParcelFromShipment(user, shipmentId, parcelId);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }


    @ApiOperation(value = "Filter shipments by warehouse")
    @GetMapping("/warehouses/{warehouseId}")
    public List<Shipment> getShipmentsByWarehouse(@RequestHeader HttpHeaders headers,
                                                  @PathVariable int warehouseId) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            return shipmentService.getShipmentsByWarehouse(user, warehouseId);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @ApiOperation(value = "Filter shipments by customer")
    @GetMapping("/customers/{customerId}")
    public List<Shipment> getShipmentsByCustomer(@RequestHeader HttpHeaders headers,
                                                 @PathVariable int customerId) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            return shipmentService.getShipmentsByCustomer(user, customerId);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @ApiOperation(value = "Show next incoming shipments.")
    @GetMapping("/arriving")
    public List<Shipment> getShipmentToArrive(@RequestHeader HttpHeaders headers) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            return shipmentService.getShipmentToArrive(user);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @ApiOperation(value = "Create shipment")
    @PostMapping
    public Shipment create(@RequestHeader HttpHeaders headers,
                          @Valid @RequestBody ShipmentDto shipmentDto) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            Shipment shipment = shipmentMapper.fromDto(shipmentDto);

            return shipmentService.create(shipment, user);

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());

        } catch (IllegalCreateException e) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());

        }

    }

    @ApiOperation(value = "Update shipment")
    @PutMapping("/{id}")
    public Shipment update(@RequestHeader HttpHeaders headers, @PathVariable int id,
                           @Valid @RequestBody ShipmentDto shipmentDto) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            Shipment shipment = shipmentMapper.fromDto(id, shipmentDto);

            return shipmentService.update(shipment, user);

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @ApiOperation(value = "Delete shipment")
    @DeleteMapping("/{id}")
    public Shipment delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {

        try {

            User user = authenticationHelper.tryGetUser(headers);

            return shipmentService.delete(id, user);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
