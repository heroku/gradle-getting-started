package com.deliverit.controllers.mvc;

import com.deliverit.controllers.authentications.AuthenticationHelper;
import com.deliverit.exceptions.*;
import com.deliverit.modelmappers.WarehouseMapper;
import com.deliverit.models.*;
import com.deliverit.models.dto.AddressDto;
import com.deliverit.models.dto.WarehouseDto;
import com.deliverit.services.contracts.CityService;
import com.deliverit.services.contracts.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/warehouses")
public class WarehouseMvcController {

    private final WarehouseService warehouseService;
    private final WarehouseMapper warehouseMapper;
    private final CityService cityService;
    private final AuthenticationHelper authenticationHelper;


    @Autowired
    public WarehouseMvcController(WarehouseService warehouseService,
                                  WarehouseMapper warehouseMapper,
                                  CityService cityService,
                                  AuthenticationHelper authenticationHelper) {
        this.warehouseService = warehouseService;
        this.warehouseMapper = warehouseMapper;
        this.cityService = cityService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("cities")
    public List<City> getAllCities() {
        return cityService.getAll();
    }

    @GetMapping
    public String getAll(Model model, HttpSession session) {

        try {

            model.addAttribute("warehouses", warehouseService.getAll(Optional.empty(),
                    Optional.empty(), Optional.empty()));

            User currentUser = authenticationHelper.tryGetUser(session);

            if (currentUser.isEmployee()) {

                return "warehouses";

            } else if (currentUser.isCustomer()) {

                return "users-warehouse-locations";

            }

        } catch (AuthenticationFailureException e) {
            return "not-found";
        }
        return "not-found";
    }

    @GetMapping("/new")
    public String showNewWarehousePage(Model model, HttpSession session) {

        try {

            User currentUser = authenticationHelper.tryGetUser(session);

            isEmployee(currentUser);

            model.addAttribute("address", new AddressDto());
            return "warehouse-new";

        } catch (AuthenticationFailureException | UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("address") AddressDto addressDto,Model model, HttpSession session,
                         BindingResult errors) {

        try {
            User currentUser = authenticationHelper.tryGetUser(session);

            isEmployee(currentUser);

            if (errors.hasErrors()) {
                return "warehouse-new";
            }

            WarehouseDto warehouseDto = new WarehouseDto();
            warehouseDto.setAddress(addressDto);

            Warehouse warehouse = warehouseMapper.fromDto(warehouseDto);
            warehouseService.create(warehouse, currentUser);
            return "redirect:/warehouses";
        } catch (AuthenticationFailureException e) {
            return "not-found";
        }catch (DuplicateEntityException e) {
           model.addAttribute("conflict",e.getMessage());
            return "conflict";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditWarehousePage(@PathVariable int id, Model model, HttpSession session) {

        try {
            User currentUser = authenticationHelper.tryGetUser(session);

            isEmployee(currentUser);

            Warehouse warehouse = warehouseService.getById(id, currentUser);
            AddressDto addressDto = warehouseMapper.toDto(warehouse);
            model.addAttribute("warehouseId", id);
            model.addAttribute("address", addressDto);
            return "warehouse-update";
        } catch (EntityNotFoundException | AuthenticationFailureException e) {
            return "not-found";
        }
    }

    @PostMapping("/{id}/update")
    public String updateWarehouse(@PathVariable int id,
                                  @Valid @ModelAttribute("address") AddressDto addressDto,
                                  BindingResult errors,
                                  HttpSession session) {

        try {
            User currentUser = authenticationHelper.tryGetUser(session);

            isEmployee(currentUser);

            if (errors.hasErrors()) {
                return "warehouse-update";
            }
            WarehouseDto warehouseDto = new WarehouseDto();
            warehouseDto.setAddress(addressDto);
            Warehouse warehouse = warehouseMapper.fromDto(id, warehouseDto);
            warehouseService.update(warehouse, currentUser);
            return "redirect:/warehouses";
        } catch (EntityNotFoundException | AuthenticationFailureException e) {
            return "not-found";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteWarehouse(@PathVariable int id, Model model, HttpSession session) {

        try {

            User currentUser = authenticationHelper.tryGetUser(session);

            warehouseService.delete(id, currentUser);

            return "redirect:/warehouses";

        } catch (EntityNotFoundException | AuthenticationFailureException e) {
            return "not-found";
        } catch (IllegalDeleteException e) {
            model.addAttribute("conflict", e.getMessage());
            return "conflict";
        }
    }

    private void isEmployee(User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Not authorized");
        }
    }
}

