package com.deliverit.services.contracts;

import com.deliverit.models.Role;

import java.util.List;

public interface RoleService {

    List<Role> getAll();

    Role getById(int id);

}
