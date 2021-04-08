package com.deliverit.services;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.IllegalCreateException;
import com.deliverit.models.Parcel;
import com.deliverit.models.Shipment;
import com.deliverit.models.User;
import com.deliverit.repositories.contracts.ParcelRepository;
import com.deliverit.repositories.contracts.ShipmentRepository;
import com.deliverit.repositories.contracts.WarehouseRepository;
import com.deliverit.services.contracts.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.deliverit.services.AuthorizationHelper.verifyUserIsAuthorized;

@Service
public class ShipmentServiceImpl implements ShipmentService {


    private static final String PARCEL_IS_ALREADY_IN_SHIPMENT_ERROR_MESSAGE = "Parcel with id: %s already is already in shipment";
    public static final String PARCEL_ALREADY_IN_ANOTHER_SHIPMENT_ERROR_MSG = "Parcel with id: %d is already in another shipment.";
    private static final String SHIPMENT_CREATE_ERROR_MESSAGE = "Shipment with status completed cannot be created.";
    private final ShipmentRepository shipmentRepository;
    private final ParcelRepository parcelRepository;
    private final WarehouseRepository warehouseRepository;


    @Autowired
    public ShipmentServiceImpl(ShipmentRepository shipmentRepository,
                               ParcelRepository parcelRepository,
                               WarehouseRepository warehouseRepository) {

        this.shipmentRepository = shipmentRepository;
        this.parcelRepository = parcelRepository;
        this.warehouseRepository = warehouseRepository;

    }

    @Override
    public List<Shipment> getAll(User user) {

        verifyUserIsAuthorized(user);

        return shipmentRepository.getAll();
    }

    @Override
    public List<Shipment> getAll() {
        return shipmentRepository.getAll();
    }

    @Override
    public Shipment getById(int id, User user) {

        verifyUserIsAuthorized(user);

        return shipmentRepository.getById(id);
    }

    @Override
    public List<Shipment> getShipmentsByCustomer(User user, int customerId) {

        verifyUserIsAuthorized(user);

        return shipmentRepository.getShipmentsByCustomer(customerId);
    }

    @Override
    public void addParcelToShipment(User user, int shipmentId, int parcelId) {

        verifyUserIsAuthorized(user);

        Shipment shipment = shipmentRepository.getById(shipmentId);

        Set<Parcel> parcelSet = shipment.getParcels();

        Parcel parcel = parcelRepository.getById(parcelId);

        if (parcelSet.contains(parcel)) {

            throw new DuplicateEntityException(String.format(PARCEL_IS_ALREADY_IN_SHIPMENT_ERROR_MESSAGE, parcelId));

        }

        boolean isParcelInAnotherShipment = true;

        try {

            shipmentRepository.getShipmentsByParcel(parcelId);

        } catch (EntityNotFoundException e) {

            isParcelInAnotherShipment = false;

        }

        if (isParcelInAnotherShipment) {

            throw new DuplicateEntityException(String.format(PARCEL_ALREADY_IN_ANOTHER_SHIPMENT_ERROR_MSG, parcelId));

        }

        parcelSet.add(parcel);

        shipment.setParcels(parcelSet);

        shipmentRepository.update(shipment);

    }

    @Override
    public void removeParcelFromShipment(User user, int shipmentId, int parcelId) {

        verifyUserIsAuthorized(user);

        Shipment shipment = shipmentRepository.getById(shipmentId);

        Set<Parcel> parcelSet = shipment.getParcels();

        Parcel parcel = parcelRepository.getById(parcelId);

        if (!parcelSet.contains(parcel)) {

            throw new EntityNotFoundException("Parcel", parcelId);

        }

        parcelSet.remove(parcel);

        shipment.setParcels(parcelSet);

        shipmentRepository.update(shipment);

    }

    @Override
    public List<Shipment> getShipmentToArrive(User user) {

        verifyUserIsAuthorized(user);

        return shipmentRepository.getShipmentToArrive();
    }

    @Override
    public List<Shipment> getShipmentsByWarehouse(User user, int warehouseId) {

        verifyUserIsAuthorized(user);

        warehouseRepository.getById(warehouseId);

        return shipmentRepository.getShipmentsByWarehouse(warehouseId);

    }

    @Override
    public Shipment create(Shipment shipment, User user) {

        verifyUserIsAuthorized(user);

        if (shipment.getStatus().getName().equalsIgnoreCase("Completed")){

            throw new IllegalCreateException(SHIPMENT_CREATE_ERROR_MESSAGE);

        }

        return shipmentRepository.create(shipment);
    }

    @Override
    public Shipment update(Shipment shipment, User user) {

        verifyUserIsAuthorized(user);

        return shipmentRepository.update(shipment);
    }

    @Override
    public Shipment delete(int id, User user) {

        verifyUserIsAuthorized(user);

        try {

            shipmentRepository.getById(id);

        } catch (EntityNotFoundException e) {

            throw new EntityNotFoundException("Shipment", id);

        }
        return shipmentRepository.delete(id);
    }


}
