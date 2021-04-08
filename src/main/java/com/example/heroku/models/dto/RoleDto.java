package com.deliverit.models.dto;

import javax.validation.constraints.Positive;

public class RoleDto {

    public static final String ROLE_ID_ERROR_MESSAGE = "Role ID must be positive.";

    @Positive(message = ROLE_ID_ERROR_MESSAGE)
    private int roleId;

    public RoleDto() {
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}
