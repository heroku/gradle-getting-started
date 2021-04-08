package com.deliverit.controllers.rest;

import com.deliverit.controllers.authentications.AuthenticationHelper;
import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.IllegalDeleteException;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.Parcel;
import com.deliverit.models.Shipment;
import com.deliverit.models.User;
import com.deliverit.models.dto.RoleDto;
import com.deliverit.models.dto.UserDto;
import com.deliverit.services.contracts.UserService;
import com.deliverit.modelmappers.UserMapper;
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
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;
    private final UserMapper userMapper;

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserController(UserService service,
                          UserMapper userMapper,
                          AuthenticationHelper authenticationHelper) {

        this.service = service;
        this.userMapper = userMapper;
        this.authenticationHelper = authenticationHelper;

    }

    @ApiOperation(value = "Get registered customers")
    @GetMapping("/count")
    public Long getCustomersCount() {

        return service.getCustomersCount();

    }


    @ApiOperation(value = "Get all customers and employees")
    @GetMapping
    public List<User> getAll(@RequestHeader HttpHeaders headers) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            return service.getAll(user);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }


    }

    @ApiOperation(value = "Get by id")
    @GetMapping("/{id}")
    public User getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            return service.getById(user, id);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        }
    }

    @ApiOperation(value = "Search for customer by email,first or last name")
    @GetMapping("/search")
    public List<User> filter(@RequestHeader HttpHeaders headers,
                             @RequestParam(required = false) Optional<String> email,
                             @RequestParam(required = false) Optional<String> firstName,
                             @RequestParam(required = false) Optional<String> lastName,
                             @RequestParam(required = false) Optional<String> word) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            return service.filter(user, email, firstName, lastName, word);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @ApiOperation(value = "Get customer incoming parcels")
    @GetMapping("/{customerId}/parcels")
    public List<Parcel> getIncomingCustomerParcels(@RequestHeader HttpHeaders headers,
                                                   @PathVariable int customerId) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            return service.getIncomingCustomerParcels(customerId, user);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @ApiOperation(value = "Get the status of a parcel")
    @GetMapping("/{customerId}/parcel/{parcelId}")
    public Shipment getStatusOfParcel(@RequestHeader HttpHeaders headers,
                                      @PathVariable int customerId,
                                      @PathVariable int parcelId) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            return service.getStatusOfParcel(customerId, parcelId, user);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @ApiOperation(value = "Create customer")
    @PostMapping
    public User create(@Valid @RequestBody UserDto userDto) {

        try {

            User user = userMapper.fromDto(userDto);

            return service.create(user);

        } catch (DuplicateEntityException e) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @ApiOperation(value = "Add role to user")
    @PutMapping("/{userId}/role")
    public void addRoleToUserRoleList(@RequestHeader HttpHeaders headers,
                                      @PathVariable int userId,
                                     @Valid @RequestBody RoleDto roleDto) {


        User user = authenticationHelper.tryGetUser(headers);

        int roleId = roleDto.getRoleId();

        try {

            service.addRoleToUserRoles(userId, roleId, user);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        } catch (DuplicateEntityException e) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());

        }
    }


    @ApiOperation(value = "Update customer")
    @PutMapping("/{id}")
    public User update(@RequestHeader HttpHeaders headers,
                       @PathVariable int id,
                       @Valid @RequestBody UserDto userDto) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            User userToUpdate = userMapper.fromDto(id, userDto);

            return service.update(userToUpdate, user);

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());

        } catch (DuplicateEntityException e) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());

        }
    }

    @ApiOperation(value = "Delete customer")
    @DeleteMapping("/{id}")
    public User delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {

        User user = authenticationHelper.tryGetUser(headers);

        try {

            return service.delete(user, id);

        } catch (UnauthorizedOperationException e) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());

        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        } catch (IllegalDeleteException e) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());

        }
    }

}
