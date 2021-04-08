package com.deliverit.repositories.contracts;

import com.deliverit.models.Parcel;
import com.deliverit.models.Shipment;

import java.util.List;
import java.util.Optional;

public interface ShipmentRepository extends CrudOperationsRepository<Shipment>, SimpleOperationRepository<Shipment> {


    List<Shipment> getShipmentsByCustomer(int customerId);

    List<Shipment> getShipmentsByWarehouse(int warehouseId);

    List<Shipment> getShipmentToArrive();

    Shipment getShipmentsByParcel(int parcelId);

}
