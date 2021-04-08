package com.deliverit.repositories.contracts;

import com.deliverit.models.Status;

import java.util.List;

public interface StatusRepository {

    Status getById(int id);

    List<Status> getALl();

}
