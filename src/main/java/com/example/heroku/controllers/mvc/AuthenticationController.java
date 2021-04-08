package com.deliverit.controllers.mvc;


import com.deliverit.controllers.authentications.AuthenticationHelper;
import com.deliverit.exceptions.AuthenticationFailureException;
import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.modelmappers.UserMapper;
import com.deliverit.models.City;
import com.deliverit.models.User;
import com.deliverit.models.dto.LoginDto;
import com.deliverit.models.dto.RegisterDto;
import com.deliverit.services.contracts.CityService;
import com.deliverit.services.contracts.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {


    private final UserService userService;
    private final UserMapper userMapper;
    private final CityService cityService;
    private final AuthenticationHelper authenticationHelper;


    public AuthenticationController(UserService userService, UserMapper userMapper, CityService cityService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.cityService = cityService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("cities")
    public List<City> getAllAddresses() {
        return cityService.getAll();
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto login,
                              Model model,
                              BindingResult bindingResult,
                              HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            authenticationHelper.verifyAuthentication(login.getEmail(), login.getPassword());
            session.setAttribute("currentUser", login.getEmail());
            User currentUser = authenticationHelper.tryGetUser(session);
            model.addAttribute("currentUser",currentUser);
            return "redirect:/";
        } catch (AuthenticationFailureException e) {
            bindingResult.rejectValue("email", "auth_error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/";
    }


    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new RegisterDto());
        return "register";
    }


    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterDto register,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (!register.getPassword().equals(register.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password_error", "Password confirmation should match password.");
            return  "register";
        }

        try {
            User user = userMapper.fromDto(register);
            userService.create(user);
            return "redirect:/";
        } catch (DuplicateEntityException e) {
            bindingResult.rejectValue("email", "email_error", e.getMessage());
            return "register";
        }
    }

}
