package com.deliverit.controllers.authentications;

import com.deliverit.exceptions.AuthenticationFailureException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.User;
import com.deliverit.services.contracts.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;

@Component
public class AuthenticationHelper {
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String AUTHORIZATION_ERROR_MESSAGE = "The requested resource requires authentication.";
    public static final String INVALID_EMAIL_ERROR_MESSAGE = "Invalid email address.";

    public static final String AUTHENTICATION_FAILURE_MESSAGE = "Wrong email or password.";

    private final UserService userService;

    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, AUTHORIZATION_ERROR_MESSAGE);
        }

        try {
            String email = headers.getFirst(AUTHORIZATION_HEADER_NAME);
            return userService.getByEmail(email);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, INVALID_EMAIL_ERROR_MESSAGE);
        }
    }

    public User tryGetUser(HttpSession session) {
        String currentUserEmail = (String) session.getAttribute("currentUser");

        if (currentUserEmail == null) {
            throw new UnauthorizedOperationException("No user logged in.");
        }

        return userService.getByEmail(currentUserEmail);
    }

    public User verifyAuthentication(String email, String password) {
        try {
            User user = userService.getByEmail(email);
            if (!user.getPassword().equals(password)) {
                throw new AuthenticationFailureException(AUTHENTICATION_FAILURE_MESSAGE);
            }
            return user;
        } catch (EntityNotFoundException e) {
            throw new AuthenticationFailureException(AUTHENTICATION_FAILURE_MESSAGE);
        }
    }

}
