package com.deliverit.services;

import com.deliverit.models.Status;
import com.deliverit.repositories.contracts.StatusRepository;
import com.deliverit.services.contracts.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusServiceImpl implements StatusService {


    private final StatusRepository statusRepository;

    @Autowired
    public StatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }


    @Override
    public Status getById(int id) {
        return statusRepository.getById(id);
    }

    @Override
    public List<Status> getAll() {
        return statusRepository.getALl();
    }
}
