package com.deliverit.controllers.mvc;

import com.deliverit.controllers.authentications.AuthenticationHelper;
import com.deliverit.exceptions.*;
import com.deliverit.modelmappers.ParcelMapper;
import com.deliverit.modelmappers.UserMapper;
import com.deliverit.models.*;
import com.deliverit.models.dto.ParcelDto;
import com.deliverit.models.dto.ParcelTransferDto;
import com.deliverit.models.dto.UserTransferDataDto;
import com.deliverit.repositories.contracts.CategoryRepository;
import com.deliverit.services.contracts.ParcelService;
import com.deliverit.services.contracts.ShipmentService;
import com.deliverit.services.contracts.UserService;
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
@RequestMapping("/parcels")
public class ParcelMvcController {


    private final ParcelService parcelService;
    private final ParcelMapper parcelMapper;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final WarehouseService warehouseService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;
    private final ShipmentService shipmentService;


    @Autowired
    public ParcelMvcController(ParcelService parcelService,
                               ParcelMapper parcelMapper,
                               UserService userService,
                               CategoryRepository categoryRepository,
                               WarehouseService warehouseService,
                               AuthenticationHelper authenticationHelper,
                               UserMapper userMapper, ShipmentService shipmentService) {

        this.parcelService = parcelService;
        this.parcelMapper = parcelMapper;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
        this.warehouseService = warehouseService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
        this.shipmentService = shipmentService;
    }


    @ModelAttribute("categories")
    public List<Category> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    @ModelAttribute("warehouses")
    public List<Warehouse> getAllWarehouses() {
        return warehouseService.getAll(Optional.empty(), Optional.empty(), Optional.empty());
    }

    @ModelAttribute("customers")
    public List<User> getAllCustomer() {
        return userService.getAllCustomers();
    }

    @GetMapping
    public String getAllParcels(Model model, HttpSession session) {


        try {

            User currentUser = authenticationHelper.tryGetUser(session);

            isEmployee(currentUser);

            List<ParcelTransferDto> parcelTransferDtoList = parcelMapper.convertAll(shipmentService.getAll(currentUser),
                    parcelService.getAll(currentUser));

            model.addAttribute("parcels", parcelTransferDtoList);
            return "parcels";

        } catch (AuthenticationFailureException | UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @GetMapping("/user")
    public String getCustomerParcels(Model model, HttpSession session) {


        try {

            User currentUser = authenticationHelper.tryGetUser(session);

            UserTransferDataDto userTransferDataDto = userMapper.toDto(currentUser);

            List<ParcelTransferDto> parcelTransferDtoList = parcelMapper.convertAll(shipmentService.getAll(),
                    parcelService.getParcelsByCustomers(currentUser));

            model.addAttribute("currentUser", userTransferDataDto);
            model.addAttribute("parcels", parcelTransferDtoList);

            return "user-parcels";

        } catch (AuthenticationFailureException e) {
            return "not-found";
        }
    }


    @GetMapping("/{id}")
    public String getById(@PathVariable int id, Model model, HttpSession session) {


        try {
            User currentUser = authenticationHelper.tryGetUser(session);

            isEmployee(currentUser);

            Parcel parcel = parcelService.getById(currentUser, id);
            model.addAttribute("shipments", shipmentService.getAll());
            model.addAttribute("parcel", parcel);
            return "parcel";

        }catch (AuthenticationFailureException | UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @GetMapping("{parcelId}/shipment/{shipmentId}")
    public String addParcelToShipment(@PathVariable int parcelId, @PathVariable int shipmentId, HttpSession session,Model model) {


        try {
            User currentUser = authenticationHelper.tryGetUser(session);

            isEmployee(currentUser);

            shipmentService.addParcelToShipment(currentUser, shipmentId, parcelId);

            return "redirect:/shipments/{shipmentId}";
        } catch (AuthenticationFailureException | EntityNotFoundException | UnauthorizedOperationException e) {
            return "not-found";
        } catch (DuplicateEntityException e) {
            model.addAttribute("conflict",e.getMessage());
            return "conflict";
        }
    }


    @GetMapping("/new")
    public String showNewParcelPage(Model model, HttpSession session) {


        try {
            User currentUser = authenticationHelper.tryGetUser(session);

            isEmployee(currentUser);

            model.addAttribute("parcel", new ParcelDto());

            return "parcel-new";

        } catch (AuthenticationFailureException | UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @PostMapping("/new")
    public String createParcel(@Valid @ModelAttribute("parcel") ParcelDto parcelDto,
                               BindingResult errors,
                               HttpSession session) {

        try {

            User currentUser = authenticationHelper.tryGetUser(session);

            isEmployee(currentUser);

            if (errors.hasErrors()) {
                return "parcel-new";
            }

            Parcel parcel = parcelMapper.fromDto(parcelDto);
            parcelService.create(parcel, currentUser);

            return "redirect:/parcels";
        } catch (AuthenticationFailureException | EntityNotFoundException | UnauthorizedOperationException e) {
            return "not-found";
        }
    }


    @GetMapping("/{id}/update")
    public String showEditParcelPage(@PathVariable int id, Model model, HttpSession session) {

        try {
            User currentUser = authenticationHelper.tryGetUser(session);

            isEmployee(currentUser);

            Parcel parcel = parcelService.getById(currentUser, id);
            ParcelDto parcelDto = parcelMapper.toDto(parcel);
            model.addAttribute("parcelId", id);
            model.addAttribute("parcel", parcelDto);

            return "parcel-update";
        } catch (AuthenticationFailureException | EntityNotFoundException | UnauthorizedOperationException e) {
            return "not-found";
        }

    }

    @PostMapping("/{id}/update")
    public String updateParcel(@PathVariable int id,
                               @Valid @ModelAttribute("parcel") ParcelDto parcelDto,
                               BindingResult errors,
                               HttpSession session) {

        try {
            User currentUser = authenticationHelper.tryGetUser(session);

            isEmployee(currentUser);

            if (errors.hasErrors()) {
                return "parcel-update";
            }

            Parcel parcel = parcelMapper.fromDto(id, parcelDto);
            parcelService.update(parcel, currentUser);

            return "redirect:/parcels";
        }catch (AuthenticationFailureException | EntityNotFoundException | UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteParcel(@PathVariable int id, Model model, HttpSession session) {


        try {

            User currentUser = authenticationHelper.tryGetUser(session);

            parcelService.delete(id, currentUser);

            return "redirect:/parcels";

        } catch (AuthenticationFailureException | EntityNotFoundException | UnauthorizedOperationException e) {
            return "not-found";
        } catch (IllegalDeleteException e) {
            model.addAttribute("conflict",e.getMessage());
            return "conflict";
        }
    }

    private void isEmployee(User user) {
        if (!user.isEmployee()) {
            throw new UnauthorizedOperationException("Not authorized");
        }
    }

}
