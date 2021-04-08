package com.deliverit.modelmappers;


import com.deliverit.models.Address;
import com.deliverit.models.City;
import com.deliverit.models.Parcel;
import com.deliverit.models.Warehouse;
import com.deliverit.models.dto.AddressDto;
import com.deliverit.models.dto.ParcelDto;
import com.deliverit.models.dto.WarehouseDto;
import com.deliverit.repositories.contracts.WarehouseRepository;
import com.deliverit.services.contracts.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WarehouseMapper {


    private final WarehouseRepository warehouseRepository;
    private final CityService cityService;

    @Autowired
    public WarehouseMapper(WarehouseRepository warehouseRepository, CityService cityService) {

        this.warehouseRepository = warehouseRepository;
        this.cityService = cityService;
    }


    public Warehouse fromDto(WarehouseDto warehouseDto) {

        Warehouse warehouse = new Warehouse();

        dtoToObject(warehouseDto, warehouse);

        return warehouse;
    }

    public Warehouse fromDto(int id, WarehouseDto warehouseDto) {

        Warehouse warehouse = warehouseRepository.getById(id);

        dtoToObject(warehouseDto, warehouse);

        return warehouse;
    }
    public AddressDto toDto(Warehouse warehouse) {
        AddressDto addressDto = new AddressDto();
        addressDto.setCityId(warehouse.getAddress().getCity().getId());
        addressDto.setStreetName(warehouse.getAddress().getStreetName());

        return addressDto;
    }

    private void dtoToObject(WarehouseDto warehouseDto, Warehouse warehouse) {

        City city = cityService.getById(warehouseDto.getAddress().getCityId());

        Address address = new Address(warehouseDto.getAddress().getStreetName(), city);

        warehouse.setAddress(address);
    }
}
