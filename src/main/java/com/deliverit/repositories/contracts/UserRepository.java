package com.deliverit.repositories.contracts;

import com.deliverit.models.Shipment;
import com.deliverit.models.User;
import com.deliverit.models.Parcel;

import java.util.Optional;

import java.util.List;

public interface UserRepository extends SimpleOperationRepository<User>, CrudOperationsRepository<User> {


    User getByEmail(String email);

    Long getCustomersCount();

    List<User> filter(Optional<String> email, Optional<String> firstName, Optional<String> lastName);

    List<Parcel> getIncomingCustomerParcels(int id);

    List<Parcel> getCustomerParcels(int id);

    List<User> filterByOneWord(Optional<String> word);

    Shipment getStatusOfParcel(int parcelId);

    List<User> getAllCustomers();
}
