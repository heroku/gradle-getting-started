package com.deliverit.services;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.IllegalCreateException;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.*;
import com.deliverit.repositories.contracts.ParcelRepository;
import com.deliverit.repositories.contracts.ShipmentRepository;
import com.deliverit.repositories.contracts.WarehouseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.deliverit.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class ShipmentServiceImplTests {


    @Mock
    ShipmentRepository shipmentRepository;

    @Mock
    ParcelRepository parcelRepository;

    @Mock
    WarehouseRepository warehouseRepository;

    @InjectMocks
    ShipmentServiceImpl shipmentService;


    @Test
    public void getAll_Should_ThrowException_When_UserIsNotEmployee() {

        User user = createMockUser();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> shipmentService.getShipmentsByCustomer(user, 3));

    }

    @Test
    public void getAll_Should_ReturnAllShipments_When_ShipmentsExist() {

        Shipment shipment = createMockShipment();

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        List<Shipment> result = new ArrayList<>();

        result.add(shipment);

        Mockito.when(shipmentRepository.getAll())
                .thenReturn(result);

        shipmentService.getAll(user);

        Mockito.verify(shipmentRepository, Mockito.times(1))
                .getAll();

    }

    @Test
    public void getById_Should_ThrowException_When_UserIsNotEmployee() {

        User user = createMockUser();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> shipmentService.getShipmentsByCustomer(user, 3));

    }

    @Test
    public void getById_Should_ReturnShipment_When_ShipmentsExist() {

        Shipment shipment = createMockShipment();

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Mockito.when(shipmentRepository.getById(shipment.getId()))
                .thenReturn(shipment);

        Shipment result = shipmentService.getById(shipment.getId(), user);

        Assertions.assertEquals(1, result.getId());
    }

    @Test
    public void getShipmentsByCustomer_Should_ThrowException_When_UserIsNotEmployee() {

        User user = createMockUser();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> shipmentService.getShipmentsByCustomer(user, 3));

    }

    @Test
    public void getShipmentsByCustomer_Should_ReturnShipments_When_UserIsEmployee() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        User customer = createMockUser();

        List<Shipment> result = new ArrayList<>();

        Mockito.when(shipmentRepository.getShipmentsByCustomer(customer.getId()))
                .thenReturn(result);

        shipmentService.getShipmentsByCustomer(user, customer.getId());

        Mockito.verify(shipmentRepository, Mockito.times(1))
                .getShipmentsByCustomer(customer.getId());

    }

    @Test
    public void addParcelToShipment_Should_ThrowException_When_UserIsNotEmployee() {

        User user = createMockUser();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> shipmentService.addParcelToShipment(user, 1, 1));

    }

    @Test
    public void addParcelToShipment_Should_ThrowException_When_ParcelIsInShipmentList() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Shipment shipment = createMockShipment();

        Parcel parcel = createMockParcel();

        shipment.setParcels(Set.of(parcel));

        Mockito.when(shipmentRepository.getById(shipment.getId())).thenReturn(shipment);

        Mockito.when(parcelRepository.getById(parcel.getId())).thenReturn(parcel);

        Assertions.assertThrows(DuplicateEntityException.class,
                () -> shipmentService.addParcelToShipment(user, shipment.getId(), parcel.getId()));

    }

    @Test
    public void addParcelToShipment_Should_Add_When_ParcelIsNotInShipmentList() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Shipment shipment = createMockShipment();

        Parcel parcel = createMockParcel();

        Set<Parcel> parcels = new HashSet<>();

        shipment.setParcels(parcels);

        Mockito.when(shipmentRepository.getById(shipment.getId())).thenReturn(shipment);

        Mockito.when(parcelRepository.getById(parcel.getId())).thenReturn(parcel);

        Mockito.when(shipmentRepository.getShipmentsByParcel(parcel.getId())).thenThrow(EntityNotFoundException.class);

        shipmentService.addParcelToShipment(user, shipment.getId(), parcel.getId());

        Mockito.verify(shipmentRepository, Mockito.times(1)).update(shipment);

    }

    @Test
    public void removeParcelToShipment_Should_ThrowException_When_ParcelIsNotInShipmentList() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Shipment shipment = createMockShipment();

        Parcel parcel = createMockParcel();

        Parcel anotherParcel = createMockParcel();

        anotherParcel.setId(10);

        shipment.setParcels(Set.of(anotherParcel));

        Mockito.when(shipmentRepository.getById(shipment.getId())).thenReturn(shipment);

        Mockito.when(parcelRepository.getById(parcel.getId())).thenReturn(parcel);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> shipmentService.removeParcelFromShipment(user, shipment.getId(), parcel.getId()));

    }

    @Test
    public void removeParcelToShipment_Should_RemoveParcel_When_ParcelIsInShipmentList() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Shipment shipment = createMockShipment();

        Parcel parcel = createMockParcel();

        Set<Parcel> parcels = new HashSet<>();

        parcels.add(parcel);

        shipment.setParcels(parcels);

        Mockito.when(shipmentRepository.getById(shipment.getId())).thenReturn(shipment);

        Mockito.when(parcelRepository.getById(parcel.getId())).thenReturn(parcel);

        shipmentService.removeParcelFromShipment(user, shipment.getId(), parcel.getId());

        Mockito.verify(shipmentRepository, Mockito.times(1)).update(shipment);

    }

    @Test
    public void shipmenToArrive_Should_ReturnShipments_When_UserIsAuthorized() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        List<Shipment> shipmentList = new ArrayList<>();

        Mockito.when(shipmentRepository.getShipmentToArrive()).thenReturn(shipmentList);

        shipmentService.getShipmentToArrive(user);

        Mockito.verify(shipmentRepository, Mockito.times(1)).getShipmentToArrive();

    }


    @Test
    public void getShipmentsByWarehouse_Should_ThrowException_When_UserIsNotEmployee() {

        User user = createMockUser();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> shipmentService.getShipmentsByWarehouse(user, 3));

    }

    @Test
    public void getShipmentsByWarehouse_Should_ReturnShipments_When_UserIsEmployee() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Warehouse warehouse = createMockWarehouse();

        List<Shipment> result = new ArrayList<>();

        Mockito.when(shipmentRepository.getShipmentsByWarehouse(warehouse.getId()))
                .thenReturn(result);

        Mockito.when(warehouseRepository.getById(warehouse.getId())).thenReturn(warehouse);

        shipmentService.getShipmentsByWarehouse(user, warehouse.getId());

        Mockito.verify(shipmentRepository, Mockito.times(1))
                .getShipmentsByWarehouse(warehouse.getId());

    }

    @Test
    public void create_Should_ThrowException_When_UserIsNotEmployee() {

        User user = createMockUser();

        Shipment shipment = createMockShipment();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> shipmentService.create(shipment, user));

    }

    @Test
    public void create_Should_ThrowException_When_StatusIsCompleted(){

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Shipment shipment = createMockShipment();

        shipment.setStatus(new Status(1,"Completed"));

        Assertions.assertThrows(IllegalCreateException.class,
                () -> shipmentService.create(shipment,user));

    }


    @Test
    public void create_Should_createShipment_When_UserIsEmployee() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Shipment shipment = createMockShipment();

        Mockito.when(shipmentRepository.create(shipment))
                .thenReturn(shipment);

        shipmentService.create(shipment, user);

        Mockito.verify(shipmentRepository, Mockito.times(1)).create(shipment);

    }


    @Test
    public void update_Should_ThrowException_When_UserIsNotEmployee() {

        User user = createMockUser();

        Shipment shipment = createMockShipment();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> shipmentService.update(shipment, user));

    }


    @Test
    public void update_Should_UpdateShipment_When_Exist() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Shipment shipment = createMockShipment();

        shipmentService.update(shipment, user);

        Mockito.verify(shipmentRepository, Mockito.times(1)).update(shipment);

    }

    @Test
    public void delete_Should_ThrowException_When_UserIsNotEmployee() {

        User user = createMockUser();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> shipmentService.delete(1, user));

    }

    @Test
    public void delete_Should_ThrowException_When_ShipmentNotExist() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Shipment shipment = createMockShipment();

        Mockito.when(shipmentRepository.getById(shipment.getId()))
                .thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> shipmentService.delete(shipment.getId(), user));

    }

    @Test
    public void delete_Should_DeleteShipment_When_Exist() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Shipment shipment = createMockShipment();

        Mockito.when(shipmentRepository.delete(shipment.getId()))
                .thenReturn(shipment);

        shipmentService.delete(shipment.getId(), user);

        Mockito.verify(shipmentRepository, Mockito.times(1))
                .delete(shipment.getId());

    }
}
