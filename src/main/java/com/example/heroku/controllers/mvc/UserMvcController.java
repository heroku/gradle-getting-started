package com.deliverit.controllers.mvc;

import com.deliverit.controllers.authentications.AuthenticationHelper;
import com.deliverit.exceptions.*;
import com.deliverit.modelmappers.UserMapper;
import com.deliverit.models.User;
import com.deliverit.models.dto.AddressDto;
import com.deliverit.models.dto.EditUserPasswordDto;
import com.deliverit.models.dto.UserTransferDataDto;
import com.deliverit.services.contracts.CityService;
import com.deliverit.services.contracts.RoleService;
import com.deliverit.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;
    private final CityService cityService;
    private final RoleService roleService;

    @Autowired
    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper, UserMapper userMapper, CityService cityService, RoleService roleService) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
        this.cityService = cityService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getAll(Model model, HttpSession session) {


        try {
            User currentUser = authenticationHelper.tryGetUser(session);

            isEmployee(currentUser);
            model.addAttribute("users", userService.getAll(currentUser));
            return "users";

        } catch (AuthenticationFailureException | UnauthorizedOperationException e) {
            return "not-found";
        }

    }

    @GetMapping("/{id}/password")
    public String showEditUserPasswordPage(@PathVariable int id, HttpSession session, Model model) {


        try {

            User currentUser = authenticationHelper.tryGetUser(session);

            User user = userService.getById(currentUser, id);

            model.addAttribute("editUserPasswordDto", userMapper.editPasswordFromUser(user));

            return "edit-password";

        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @PostMapping("/{id}/password")
    public String handleEditPasswordUser(Model model,
                                         HttpSession session,
                                         @PathVariable int id,
                                         @Valid @ModelAttribute("editUserPasswordDto") EditUserPasswordDto dto,
                                         BindingResult bindingResult) {

        try {
            User currentUser = authenticationHelper.tryGetUser(session);

            if (!dto.getPreviousPassword().equals(currentUser.getPassword())) {
                bindingResult.rejectValue("previousPassword", "previousPassword_error", "Previous password not match");
            }

            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                bindingResult.rejectValue("password", "password_error", "Passwords not match");
            }

            if (bindingResult.hasErrors()) {
                return "edit-password";
            }

            User userToUpdate = userMapper.editPasswordFromDto(id, dto);
            userService.update(userToUpdate, currentUser);

            return "redirect:/users/{id}/profile";
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @GetMapping("/{id}/address")
    public String showEditUserAddressPage(@PathVariable int id, HttpSession session, Model model) {


        try {
            User currentUser = authenticationHelper.tryGetUser(session);
            User user = userService.getById(currentUser, id);
            model.addAttribute("currentUser", user);
            model.addAttribute("address", new AddressDto());
            model.addAttribute("cities", cityService.getAll());
            return "edit-address";
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @PostMapping("/{id}/address")
    public String handleEditAddressUser(Model model,
                                        HttpSession session,
                                        @PathVariable int id,
                                        @Valid @ModelAttribute("address") AddressDto dto,
                                        BindingResult bindingResult) {


        try {
            User currentUser = authenticationHelper.tryGetUser(session);

            if (bindingResult.hasErrors()) {
                return "edit-password";
            }

            User userToUpdate = userMapper.editAddressFromAddressDto(id, dto);
            userService.update(userToUpdate, currentUser);
            return "redirect:/users/{id}/profile";
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @GetMapping("/{id}/profile")
    public String showUserProfilePage(@PathVariable int id, HttpSession session, Model model) {

        try {
            User currentUser = authenticationHelper.tryGetUser(session);
            User user = userService.getById(currentUser, id);
            UserTransferDataDto userDto = userMapper.toDto(user);
            model.addAttribute("visitor", currentUser);
            model.addAttribute("currentUser", userDto);

            return "profile";
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @GetMapping("/{id}/roles")
    public String updateUserRolePage(Model model, HttpSession session, @PathVariable int id) {


        try {
            User currentUser = authenticationHelper.tryGetUser(session);
            isEmployee(currentUser);
            User user = userService.getById(currentUser, id);
            UserTransferDataDto userDto = userMapper.toDto(user);
            model.addAttribute("currentUser", userDto);
            model.addAttribute("roles", roleService.getAll());

            return "user-roles";
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            return "not-found";
        }
    }

    @PostMapping("/{id}/roles")
    public String handleAddUserRole(Model model,
                                    HttpSession session,
                                    @PathVariable int id,
                                    @ModelAttribute("currentUser") UserTransferDataDto dto,
                                    BindingResult bindingResult) {

        try {
            User currentUser = authenticationHelper.tryGetUser(session);
            isEmployee(currentUser);

            if (bindingResult.hasErrors()) {
                return "error";
            }
            User userToUpdate = userMapper.setNewRolesFromDto(dto, id);
            userService.update(userToUpdate, currentUser);
            return "redirect:/users/{id}/profile";
        } catch (EntityNotFoundException | UnauthorizedOperationException e) {
            return "not-found";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("role", "duplicate_role", e.getMessage());
            return "profile";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable int id, Model model, HttpSession session) {


        try {

            User currentUser = authenticationHelper.tryGetUser(session);

            userService.delete(currentUser, id);

            if (currentUser.isEmployee()) {

                if (currentUser.getId() == id){
                    session.removeAttribute("currentUser");
                    return "redirect:/";
                }

                return "redirect:/users";
            }else {
                session.removeAttribute("currentUser");
                return "redirect:/";
            }
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
