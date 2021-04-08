package com.deliverit.services.contracts;

import com.deliverit.models.Shipment;
import com.deliverit.models.User;
import com.deliverit.models.Parcel;

import java.util.List;
import java.util.Optional;

public interface UserService {


    List<User> getAll(User user);

    List<User> getAllCustomers();

    User getById(User user, int id);

    User getByEmail(String email);

    void addRoleToUserRoles(int userId, int roleId, User user);

    User create(User user);

    User update(User userToUpdate, User user);

    User delete(User user, int id);

    List<User> filter(User user, Optional<String> email, Optional<String> firstName,
                      Optional<String> lastName, Optional<String> word);

    List<Parcel> getIncomingCustomerParcels(int id, User user);

    Long getCustomersCount();

    Shipment getStatusOfParcel(int customerId, int parcelId, User user);
}
