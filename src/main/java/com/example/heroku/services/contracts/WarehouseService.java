package com.deliverit.services.contracts;

import com.deliverit.models.User;
import com.deliverit.models.Warehouse;

import java.util.List;
import java.util.Optional;

public interface WarehouseService {

    List<Warehouse> getAll(Optional<String> s, Optional<String> address, Optional<String> city);

    Warehouse getById(int id, User user);

    Warehouse create(Warehouse warehouse, User user);

    Warehouse update(Warehouse warehouse, User user);

    Warehouse delete(int id, User user);

}
