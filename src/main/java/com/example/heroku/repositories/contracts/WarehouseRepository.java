package com.deliverit.repositories.contracts;

import com.deliverit.models.Warehouse;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepository extends CrudOperationsRepository<Warehouse> {


    List<Warehouse> getAll(Optional<String> address, Optional<String> city, Optional<String> country);

    Warehouse getById(int id);

    Warehouse getByAddressId(Warehouse warehouse);

}
