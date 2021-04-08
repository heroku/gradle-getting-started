package com.deliverit.services;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.IllegalDeleteException;
import com.deliverit.models.Parcel;
import com.deliverit.models.User;
import com.deliverit.models.Warehouse;
import com.deliverit.repositories.contracts.ParcelRepository;
import com.deliverit.repositories.contracts.WarehouseRepository;
import com.deliverit.services.contracts.AddressService;
import com.deliverit.services.contracts.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.deliverit.services.AuthorizationHelper.verifyUserIsAuthorized;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private static final String WAREHOUSE = "Warehouse";
    private static final String WAREHOUSE_DELETE_ERROR_MESSAGE = "Warehouse with parcels in it cannot be deleted.";
    private final WarehouseRepository warehouseRepository;
    private final AddressService addressService;
    private final ParcelRepository parcelRepository;


    @Autowired
    public WarehouseServiceImpl(WarehouseRepository warehouseRepository,
                                AddressService addressService,
                                ParcelRepository parcelRepository) {

        this.warehouseRepository = warehouseRepository;
        this.addressService = addressService;
        this.parcelRepository = parcelRepository;

    }

    @Override
    public List<Warehouse> getAll(Optional<String> address, Optional<String> city, Optional<String> country) {
        return warehouseRepository.getAll(address, city, country);
    }

    @Override
    public Warehouse getById(int id, User user) {

        verifyUserIsAuthorized(user);

        return warehouseRepository.getById(id);
    }

    @Override
    public Warehouse create(Warehouse warehouse, User user) {

        verifyUserIsAuthorized(user);
        
        warehouse.setAddress(addressService.getOrCreate(warehouse.getAddress()));

        boolean duplicateExist = true;

        try {
            warehouseRepository.getByAddressId(warehouse);

        } catch (EntityNotFoundException e) {

            duplicateExist = false;

        }

        if (duplicateExist) {

            throw new DuplicateEntityException(WAREHOUSE, "address", warehouse.getAddress().getStreetName());

        }

        return warehouseRepository.create(warehouse);

    }

    @Override
    public Warehouse update(Warehouse warehouse, User user) {

        verifyUserIsAuthorized(user);

        warehouse.setAddress(addressService.getOrCreate(warehouse.getAddress()));

        return warehouseRepository.update(warehouse);
    }

    @Override
    public Warehouse delete(int id, User user) {

        verifyUserIsAuthorized(user);

        List<Parcel> parcelList = parcelRepository.filterByWarehouse(id);

        if (!parcelList.isEmpty()) {

            throw new IllegalDeleteException(WAREHOUSE_DELETE_ERROR_MESSAGE);

        }

        try {

            warehouseRepository.getById(id);

        } catch (EntityNotFoundException e) {

            throw new EntityNotFoundException(WAREHOUSE, id);

        }
        return warehouseRepository.delete(id);
    }


}
