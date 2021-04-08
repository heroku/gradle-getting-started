package com.deliverit.services.contracts;

import com.deliverit.models.Parcel;
import com.deliverit.models.User;

import java.util.List;
import java.util.Optional;

public interface ParcelService {

    List<Parcel> getAll(User user);

    Parcel getById(User user, int id);

    Parcel create(Parcel parcel, User user);

    Parcel update(Parcel parcel, User user);

    Parcel delete(int id, User user);

    List<Parcel> sort(User user, Optional<String> weight, Optional<String> date);

    List<Parcel> filter(User user, Optional<Double> weight, Optional<String> customer,
                        Optional<String> warehouse, Optional<String> category);

    List<Parcel> getParcelsByCustomers(User user);

}
