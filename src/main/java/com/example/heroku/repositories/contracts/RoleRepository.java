package com.deliverit.repositories.contracts;

import com.deliverit.models.Role;

import java.util.List;

public interface RoleRepository {
    Role getById(int id);

    List<Role> getAll();

    Role getByName(String roleName);
}
