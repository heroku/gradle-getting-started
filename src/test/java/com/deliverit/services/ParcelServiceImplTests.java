package com.deliverit.services;

import com.deliverit.Helpers;
import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.IllegalDeleteException;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.*;
import com.deliverit.repositories.contracts.ParcelRepository;
import com.deliverit.repositories.contracts.ShipmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.deliverit.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class ParcelServiceImplTests {

    @Mock
    ParcelRepository parcelRepository;

    @Mock
    ShipmentRepository shipmentRepository;

    @InjectMocks
    ParcelServiceImpl parcelService;


    @Test
    public void get_All_Should_Throw_When_UserIsNotEmployee() {

        Parcel mockParcel = Helpers.createMockParcel();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> parcelService.getAll(mockParcel.getUser()));

    }

    @Test
    public void get_By_Id_Should_Throw_When_UserIsNotEmployee() {

        Parcel mockParcel = Helpers.createMockParcel();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> parcelService.getById(mockParcel.getUser(), mockParcel.getId()));

    }

    @Test
    public void get_By_Id_Should_Return_When_UserIsEmployee() {

        Parcel mockParcel = Helpers.createMockParcel();

        User mockUser = Helpers.createMockUser();

        mockUser.setRoles(Set.of(new Role(2, "Employee")));

        Mockito.when(parcelRepository.getById(mockParcel.getId())).thenReturn(mockParcel);

        Parcel result = parcelService.getById(mockUser, mockParcel.getId());

        Assertions.assertEquals(1, result.getId());

    }


    @Test
    public void getAll_Should_Return_AllParcels() {

        Parcel mockParcel = Helpers.createMockParcel();

        User mockUser = Helpers.createMockUser();

        mockUser.setRoles(Set.of(new Role(2, "Employee")));

        List<Parcel> result = new ArrayList<>();

        result.add(mockParcel);

        Mockito.when(parcelRepository.getAll())
                .thenReturn(result);

        parcelService.getAll(mockUser);

        Mockito.verify(parcelRepository, Mockito.times(1))
                .getAll();

    }

    @Test
    public void sort_Should_Sort_When_UserIsEmployee() {

        Parcel parcel = createMockParcel();

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        List<Parcel> result = new ArrayList<>();

        result.add(parcel);

        Mockito.when(parcelRepository.sort(Optional.of("asc"), Optional.empty()))
                .thenReturn(result);


        parcelService.sort(user, Optional.of("asc"), Optional.empty());


        Mockito.verify(parcelRepository, Mockito.times(1))
                .sort(Optional.of("asc"), Optional.empty());

    }


    @Test
    public void sort_Should_Throw_When_UserIsNotEmployee() {

        User user = createMockUser();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> parcelService.sort(user, Optional.empty(), Optional.empty()));

    }

    @Test
    public void sort_Should_Throw_When_WeightIsPresentAndNotCorrectValue() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> parcelService.sort(user, Optional.of("something"), Optional.empty()));

    }

    @Test
    public void sort_Should_Throw_When_DateIsPresentAndNotCorrectValue() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> parcelService.sort(user, Optional.empty(), Optional.of("something")));

    }

    @Test
    public void filter_Should_Throw_When_UserIsNotEmployee() {

        User user = createMockUser();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> parcelService.filter(user, Optional.of(123.0), Optional.empty(),
                        Optional.of("Ivancho"), Optional.of("cloth")));

    }

    @Test
    public void filter_Should_Filter_When_UserIsEmployee() {

        Parcel parcel = createMockParcel();

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        List<Parcel> result = new ArrayList<>();

        result.add(parcel);

        Mockito.when(parcelRepository.filter(Optional.of(123.0), Optional.empty(), Optional.empty(), Optional.empty()))
                .thenReturn(result);

        parcelService.filter(user, Optional.of(123.0), Optional.empty(), Optional.empty(), Optional.empty());

        Mockito.verify(parcelRepository, Mockito.times(1))
                .filter(Optional.of(123.0), Optional.empty(), Optional.empty(), Optional.empty());

    }

    @Test
    public void create_Should_ThrowException_When_UserIsNotEmployee() {

        User user = createMockUser();

        Parcel parcel = createMockParcel();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> parcelService.create(parcel, user));

    }


    @Test
    public void create_Should_callRepository_When_ParcelIsValid() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Parcel parcel = createMockParcel();

        parcelService.create(parcel,user);

        Mockito.verify(parcelRepository,Mockito.times(1)).create(parcel);

    }

    @Test
    public void update_Should_ThrowException_When_UserIsNotEmployee() {

        User user = createMockUser();

        Parcel parcel = createMockParcel();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> parcelService.update(parcel, user));

    }


    @Test
    public void update_Should_UpdateParcel_When_Exist() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Parcel parcel = createMockParcel();

        parcelService.update(parcel, user);

        Mockito.verify(parcelRepository, Mockito.times(1)).update(parcel);

    }

    @Test
    public void delete_Should_ThrowException_When_UserIsNotEmployee() {

        User user = createMockUser();

        Parcel parcel = createMockParcel();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> parcelService.delete(parcel.getId(), user));

    }

    @Test
    public void delete_Should_ThrowException_When_ParcelNotExist() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Parcel parcel = createMockParcel();

        Mockito.when(parcelRepository.getById(parcel.getId()))
                .thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> parcelService.delete(parcel.getId(), user));

    }

    @Test
    public void delete_Should_DeleteParcel_When_Exist() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Parcel parcel = createMockParcel();

        Shipment shipment = createMockShipment();

        shipment.setStatus(new Status(1, "Completed"));

        Set<Parcel> parcels = new HashSet<>();

        parcels.add(parcel);

        shipment.setParcels(parcels);

        Mockito.when(parcelRepository.delete(parcel.getId()))
                .thenReturn(parcel);

        Mockito.when(shipmentRepository.getShipmentsByParcel(parcel.getId()))
                .thenReturn(shipment);

        parcelService.delete(parcel.getId(), user);

        Mockito.verify(parcelRepository, Mockito.times(1))
                .delete(parcel.getId());

    }

    @Test
    public void delete_Should_ThrowException_When_ShipmentStatusIsOnTheWay() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Parcel parcel = createMockParcel();

        Shipment shipment = createMockShipment();

        Shipment shipmentToUpdate = createMockShipment();

        Mockito.when(shipmentRepository.getShipmentsByParcel(parcel.getId()))
                .thenReturn(shipment);

        Assertions.assertThrows(IllegalDeleteException.class,
                () -> parcelService.delete(parcel.getId(), user));

    }

    @Test
    public void delete_Should_DeleteParcel_When_ExistAndTheyAreNotInShipment() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Parcel parcel = createMockParcel();

        Shipment shipmentToUpdate = createMockShipment();

        shipmentToUpdate.setStatus(new Status(1, "Completed"));

        Set<Parcel> parcels = new HashSet<>();

        parcels.add(parcel);

        shipmentToUpdate.setParcels(parcels);

        Mockito.when(parcelRepository.delete(parcel.getId()))
                .thenReturn(parcel);

        Mockito.when(shipmentRepository.getShipmentsByParcel(parcel.getId()))
                .thenThrow(EntityNotFoundException.class);

        parcelService.delete(parcel.getId(), user);

        Mockito.verify(parcelRepository, Mockito.times(1))
                .delete(parcel.getId());

    }
}
