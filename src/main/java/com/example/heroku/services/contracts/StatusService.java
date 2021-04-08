package com.deliverit.services.contracts;

import com.deliverit.models.Status;

import java.util.List;

public interface StatusService {
    Status getById(int id);

    List<Status> getAll();
}
