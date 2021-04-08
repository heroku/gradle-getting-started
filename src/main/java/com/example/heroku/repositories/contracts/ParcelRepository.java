package com.deliverit.repositories.contracts;

import com.deliverit.models.Parcel;
import com.deliverit.models.User;

import java.util.List;
import java.util.Optional;

public interface ParcelRepository extends CrudOperationsRepository<Parcel> {

    List<Parcel> getAll();

    Parcel getById(int id);

    List<Parcel> sort(Optional<String> weight, Optional<String> date);

    List<Parcel> filter(Optional<Double> weight, Optional<String> customer,
                        Optional<String> warehouse, Optional<String> category);

    List<Parcel> filterByCategory(int id);

    List<Parcel> filterByCustomer(int id);

    List<Parcel> filterByWarehouse(int id);

    List<Parcel> getParcelsByCustomers(User user);


}
