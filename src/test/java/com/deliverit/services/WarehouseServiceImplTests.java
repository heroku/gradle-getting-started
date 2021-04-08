package com.deliverit.services;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.IllegalDeleteException;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.*;
import com.deliverit.repositories.contracts.ParcelRepository;
import com.deliverit.repositories.contracts.WarehouseRepository;
import com.deliverit.services.contracts.AddressService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.deliverit.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class WarehouseServiceImplTests {


    @Mock
    WarehouseRepository warehouseRepository;

    @Mock
    AddressService addressService;

    @InjectMocks
    WarehouseServiceImpl warehouseService;

    @Mock
    ParcelRepository parcelRepository;


    @Test
    public void getAll_Should_ReturnAllWarehouses_When_WarehousesExist() {

        Warehouse warehouse = createMockWarehouse();

        List<Warehouse> result = new ArrayList<>();

        result.add(warehouse);

        Mockito.when(warehouseRepository.getAll(Optional.empty(), Optional.empty(), Optional.empty()))
                .thenReturn(result);

        warehouseService.getAll(Optional.empty(), Optional.empty(), Optional.empty());

        Mockito.verify(warehouseRepository, Mockito.times(1))
                .getAll(Optional.empty(), Optional.empty(), Optional.empty());

    }

    @Test
    public void getById_Should_ReturnWarehouse_When_MatchExist() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Warehouse warehouse = createMockWarehouse();

        Mockito.when(warehouseRepository.getById(warehouse.getId()))
                .thenReturn(warehouse);

        Warehouse result = warehouseService.getById(warehouse.getId(), user);

        Assertions.assertEquals(1, result.getId());


    }


    @Test
    public void create_Should_ThrowException_When_UserIsNotEmployee() {

        User user = createMockUser();

        Warehouse warehouse = createMockWarehouse();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> warehouseService.create(warehouse, user));

    }


    @Test
    public void create_Should_ThrowException_When_WarehouseExist() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Address address = createMockAddress();

        Mockito.when(addressService.getOrCreate(address))
                .thenReturn(address);

        Assertions.assertThrows(DuplicateEntityException.class,
                () -> warehouseService.create(new Warehouse(3, address), user));

    }

    @Test
    public void create_Should_ThrowException_When_WarehouseNotFound() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Warehouse warehouse = createMockWarehouse();

        Mockito.when(warehouseRepository.getByAddressId(warehouse))
                .thenThrow(EntityNotFoundException.class);

        warehouseService.create(warehouse, user);

        Mockito.verify(warehouseRepository, Mockito.times(1))
                .create(warehouse);

    }


    @Test
    public void update_Should_ThrowException_When_UserIsNotEmployee() {

        User user = createMockUser();

        Warehouse warehouse = createMockWarehouse();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> warehouseService.update(warehouse, user));


    }

    @Test
    public void update_Should_UpdateWarehouse_When_Exist() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Warehouse warehouse = createMockWarehouse();

        warehouseService.update(warehouse, user);

        Mockito.verify(warehouseRepository, Mockito.times(1)).update(warehouse);

    }

    @Test
    public void delete_Should_ThrowException_When_UserIsNotEmployee() {

        User user = createMockUser();

        Warehouse warehouse = createMockWarehouse();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> warehouseService.delete(warehouse.getId(), user));


    }


    @Test
    public void delete_Should_ThrowException_When_WarehouseNotExist() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Warehouse warehouse = createMockWarehouse();

        List<Parcel> parcelList = new ArrayList<>();

        Mockito.when(warehouseRepository.getById(warehouse.getId()))
                .thenThrow(EntityNotFoundException.class);

        Mockito.when(parcelRepository.filterByWarehouse(warehouse.getId())).thenReturn(parcelList);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> warehouseService.delete(warehouse.getId(), user));

    }

    @Test
    public void delete_Should_ThrowException_When_WarehouseHasParcels() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Warehouse warehouse = createMockWarehouse();

        Parcel parcel = createMockParcel();

        List<Parcel> parcelList = new ArrayList<>();

        parcelList.add(parcel);


        Mockito.when(parcelRepository.filterByWarehouse(warehouse.getId()))
                .thenReturn(parcelList);

        Assertions.assertThrows(IllegalDeleteException.class,
                () -> warehouseService.delete(warehouse.getId(), user));


    }

    @Test
    public void delete_Should_DeleteWarehouse_When_Exist() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Warehouse warehouse = createMockWarehouse();

        Mockito.when(warehouseRepository.delete(warehouse.getId()))
                .thenReturn(warehouse);

        warehouseService.delete(warehouse.getId(), user);

        Mockito.verify(warehouseRepository, Mockito.times(1)).delete(warehouse.getId());

    }
}
