package com.deliverit.controllers.mvc;


import com.deliverit.controllers.authentications.AuthenticationHelper;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.User;
import com.deliverit.services.contracts.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomeMvcController {


    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;

    public HomeMvcController(AuthenticationHelper authenticationHelper, UserService userService) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
    }

    @GetMapping
    public String showHomePage(Model model, HttpSession session){

        try {
            User currentUser = authenticationHelper.tryGetUser(session);
            model.addAttribute("currentUser", currentUser);
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("currentUser", null);
        }
        model.addAttribute("registeredCustomers",String.valueOf(userService.getCustomersCount()));

        return "index";
    }
}
