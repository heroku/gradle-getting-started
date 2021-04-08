package com.deliverit.controllers.mvc;

import com.deliverit.controllers.authentications.AuthenticationHelper;
import com.deliverit.exceptions.AuthenticationFailureException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.IllegalDeleteException;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.modelmappers.ShipmentMapper;
import com.deliverit.models.*;
import com.deliverit.models.dto.ShipmentDto;
import com.deliverit.services.contracts.ShipmentService;
import com.deliverit.services.contracts.StatusService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/shipments")
public class ShipmentMvcController {

    private final ShipmentService shipmentService;
    private final StatusService statusService;
    private final ShipmentMapper shipmentMapper;
    private final AuthenticationHelper authenticationHelper;


    public ShipmentMvcController(ShipmentService shipmentService, StatusService statusService, ShipmentMapper shipmentMapper, AuthenticationHelper authenticationHelper) {
        this.shipmentService = shipmentService;
        this.statusService = statusService;
        this.shipmentMapper = shipmentMapper;
        this.authenticationHelper = authenticationHelper;
    }


    @ModelAttribute("statuses")
    public List<Status> getAllStatuses() {
        return statusService.getAll();
    }

    @GetMapping
    public String getAllShipments(Model model, HttpSession session) {


        try {
            User currentUser = authenticationHelper.tryGetUser(session);

            isEmployee(currentUser);


            model.addAttribute("shipments", shipmentService.getAll(currentUser));
            return "shipments";

        } catch (AuthenticationFailureException | UnauthorizedOperationException e) {
            return "not-found";
        }

    }

    @GetMapping("/{id}")
    public String getById(@PathVariable int id, Model model, HttpSession session) {


        try {
            User currentUser = authenticationHelper.tryGetUser(session);

            isEmployee(currentUser);

            Shipment shipment = shipmentService.getById(id, currentUser);
            model.addAttribute("shipment", shipment);
            return "shipment";

        } catch (AuthenticationFailureException | UnauthorizedOperationException e) {
            return "not-found";
        }


    }

    @GetMapping("/{shipmentId}/parcel/{parcelId}")
    public String removeParcelFromShipment(@PathVariable int shipmentId, @PathVariable int parcelId, HttpSession session, Model model) {


        try {
            User currentUser = authenticationHelper.tryGetUser(session);

            isEmployee(currentUser);

            shipmentService.removeParcelFromShipment(currentUser, shipmentId, parcelId);

            return "redirect:/shipments/{shipmentId}";

        } catch (AuthenticationFailureException | UnauthorizedOperationException e) {
            return "not-found";
        }
    }


    @GetMapping("/new")
    public String showNewShipmentPage(Model model, HttpSession session) {


        try {

            User currentUser = authenticationHelper.tryGetUser(session);

            isEmployee(currentUser);

            model.addAttribute("shipment", new ShipmentDto());
            return "shipment-new";

        } catch (AuthenticationFailureException | UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @PostMapping("/new")
    public String createShipment(@Valid @ModelAttribute("shipment") ShipmentDto shipmentDto, BindingResult errors, Model model,
                                 HttpSession session) {

        try {

            User currentUser = authenticationHelper.tryGetUser(session);
            isEmployee(currentUser);

            if (errors.hasErrors()) {
                return "shipment-new";
            }

            Shipment shipment = shipmentMapper.fromDto(shipmentDto);
            shipmentService.create(shipment, currentUser);

            return "redirect:/shipments";
        } catch (AuthenticationFailureException | EntityNotFoundException | UnauthorizedOperationException e) {
            return "not-found";
        }
    }


    @GetMapping("/{id}/update")
    public String showEditShipmentPage(@PathVariable int id, Model model, HttpSession session) {

        try {
            User currentUser = authenticationHelper.tryGetUser(session);
            isEmployee(currentUser);
            Shipment shipment = shipmentService.getById(id, currentUser);
            ShipmentDto shipmentDto = shipmentMapper.toDto(shipment);
            model.addAttribute("shipmentId", id);
            model.addAttribute("shipment", shipmentDto);
            return "shipment-update";
        } catch (AuthenticationFailureException | EntityNotFoundException | UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @PostMapping("/{id}/update")
    public String updateWarehouse(@PathVariable int id,
                                  @Valid @ModelAttribute("shipment") ShipmentDto shipmentDto,
                                  BindingResult errors,
                                  Model model, HttpSession session) {

        try {
            User currentUser = authenticationHelper.tryGetUser(session);
            isEmployee(currentUser);
            if (errors.hasErrors()) {
                return "shipment-update";
            }

            Shipment shipment = shipmentMapper.fromDto(id, shipmentDto);
            shipmentService.update(shipment, currentUser);
            return "redirect:/shipments";
        } catch (AuthenticationFailureException | EntityNotFoundException | UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteParcel(@PathVariable int id, Model model, HttpSession session) {


        try {
            User currentUser = authenticationHelper.tryGetUser(session);

            isEmployee(currentUser);

            shipmentService.delete(id, currentUser);

            return "redirect:/shipments";
        } catch (AuthenticationFailureException | EntityNotFoundException | UnauthorizedOperationException e) {
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
