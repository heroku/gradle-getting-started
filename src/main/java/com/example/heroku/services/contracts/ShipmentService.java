package com.deliverit.services.contracts;

import com.deliverit.models.Parcel;
import com.deliverit.models.Shipment;
import com.deliverit.models.User;

import java.util.List;

public interface ShipmentService {
    List<Shipment> getAll(User user);

    List<Shipment> getAll();

    Shipment getById(int id, User user);

    Shipment create(Shipment shipment, User user);

    Shipment update(Shipment shipment, User user);

    Shipment delete(int id, User user);

    List<Shipment> getShipmentsByCustomer(User user, int customerId);

    void addParcelToShipment(User user, int shipment, int parcelId);

    List<Shipment> getShipmentsByWarehouse(User user, int warehouseId);

    void removeParcelFromShipment(User user, int shipmentId, int parcelId);

    List<Shipment> getShipmentToArrive(User user);
}
