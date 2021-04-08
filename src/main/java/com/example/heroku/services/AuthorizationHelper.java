package com.deliverit.services;

import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.User;

public class AuthorizationHelper {

    public static final String EMPLOYEE_AUTHORIZATION_ERROR_MESSAGE =
            "Only an Employee is Authorized for this operation.";
    public static final String EMPLOYEE_CUSTOMER_AUTHORIZATION_ERROR_MESSAGE =
            "Only employee or customer is authorized to make this operation.";

    public static void verifyUserIsAuthorized(User user) {

        if (!user.isEmployee()) {

            throw new UnauthorizedOperationException(EMPLOYEE_AUTHORIZATION_ERROR_MESSAGE);

        }
    }

    public static void verifyUserIsCustomerOrEmployee(User user) {

        if (!user.isEmployee() && !user.isCustomer()) {

            throw new UnauthorizedOperationException(EMPLOYEE_CUSTOMER_AUTHORIZATION_ERROR_MESSAGE);

        }
    }
}
