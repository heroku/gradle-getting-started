package com.deliverit.controllers.mvc;

import com.deliverit.controllers.authentications.AuthenticationHelper;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class EmployeeMvcController {


    private final AuthenticationHelper authenticationHelper;

    public EmployeeMvcController(AuthenticationHelper authenticationHelper) {
        this.authenticationHelper = authenticationHelper;
    }



    @GetMapping
    public String showAdminPage(Model model, HttpSession session){

        try {
            User currentUser = authenticationHelper.tryGetUser(session);
            model.addAttribute("currentUser", currentUser);

            if (currentUser.isEmployee()){
                return "admin";
            }

        } catch (UnauthorizedOperationException e) {
            model.addAttribute("currentUser", null);
        }

        return "redirect:/";
    }
}
