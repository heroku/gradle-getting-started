package com.deliverit.services;

import com.deliverit.models.Role;
import com.deliverit.repositories.contracts.RoleRepository;
import com.deliverit.services.contracts.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public List<Role> getAll() {
        return roleRepository.getAll();
    }

    @Override
    public Role getById(int id) {
        return roleRepository.getById(id);
    }
}
