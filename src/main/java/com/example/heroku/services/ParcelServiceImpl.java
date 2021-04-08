package com.deliverit.services;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.IllegalDeleteException;
import com.deliverit.models.Parcel;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Shipment;
import com.deliverit.models.User;
import com.deliverit.repositories.contracts.ParcelRepository;
import com.deliverit.repositories.contracts.ShipmentRepository;
import com.deliverit.services.contracts.ParcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.deliverit.services.AuthorizationHelper.verifyUserIsAuthorized;

@Service
public class ParcelServiceImpl implements ParcelService {


    private static final String PARCEL = "Parcel";
    private static final String ASC_OR_DESC_ERROR_MESSAGE = "%s should be in asc or desc order.";
    private static final String PARCEL_DELETE_ERROR_MESSAGE = "Parcel which is on the way cannot be deleted.";
    private final ParcelRepository parcelRepository;
    private final ShipmentRepository shipmentRepository;


    @Autowired
    public ParcelServiceImpl(ParcelRepository parcelRepository, ShipmentRepository shipmentRepository) {
        this.parcelRepository = parcelRepository;
        this.shipmentRepository = shipmentRepository;
    }


    @Override
    public List<Parcel> getAll(User user) {

        verifyUserIsAuthorized(user);

        return parcelRepository.getAll();
    }

    public List<Parcel> getParcelsByCustomers(User user){
        return parcelRepository.getParcelsByCustomers(user);
    }

    @Override
    public Parcel getById(User user, int id) {

        verifyUserIsAuthorized(user);

        return parcelRepository.getById(id);
    }

    @Override
    public List<Parcel> sort(User user, Optional<String> weight, Optional<String> date) {

        verifyUserIsAuthorized(user);

        checkIfIsPresentAndWeightOrDateOrderIsCorrect(weight);

        checkIfIsPresentAndWeightOrDateOrderIsCorrect(date);

        return parcelRepository.sort(weight, date);
    }

    private void checkIfIsPresentAndWeightOrDateOrderIsCorrect(Optional<String> order) {

        if (order.isPresent() && !order.get().equals("asc") && !order.get().equals("desc")) {

            throw new IllegalArgumentException(String.format(ASC_OR_DESC_ERROR_MESSAGE, order.get()));

        }
    }

    @Override
    public List<Parcel> filter(User user,
                               Optional<Double> weight,
                               Optional<String> customer,
                               Optional<String> warehouse,
                               Optional<String> category) {

        verifyUserIsAuthorized(user);

        return parcelRepository.filter(weight, customer, warehouse, category);
    }


    @Override
    public Parcel create(Parcel parcel, User user) {

        verifyUserIsAuthorized(user);

        return parcelRepository.create(parcel);

    }


    @Override
    public Parcel update(Parcel parcel, User user) {

        verifyUserIsAuthorized(user);

        return parcelRepository.update(parcel);
    }


    @Override
    public Parcel delete(int id, User user) {

        verifyUserIsAuthorized(user);

        Parcel parcel;

        try {

            parcel = parcelRepository.getById(id);

        } catch (EntityNotFoundException e) {

            throw new EntityNotFoundException(PARCEL, id);

        }


        boolean isInShipment = true;

        Shipment shipment = null;

        try {

            shipment = shipmentRepository.getShipmentsByParcel(id);

            if (shipment.getStatus().getName().equals("on the way")) {

                throw new IllegalDeleteException(PARCEL_DELETE_ERROR_MESSAGE);

            }

        } catch (EntityNotFoundException e) {

            isInShipment = false;

        }

        if (isInShipment) {

            deleteParcelFromShipment(shipment, parcel);

        }

        return parcelRepository.delete(id);

    }

    public void deleteParcelFromShipment(Shipment shipment, Parcel parcel) {

        Set<Parcel> parcelSet = shipment.getParcels();

        parcelSet.remove(parcel);

        shipment.setParcels(parcelSet);

        shipmentRepository.update(shipment);

    }

}
