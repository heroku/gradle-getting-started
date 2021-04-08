package com.deliverit.modelmappers;

import com.deliverit.models.Shipment;
import com.deliverit.models.Warehouse;
import com.deliverit.models.dto.AddressDto;
import com.deliverit.models.dto.ShipmentDto;
import com.deliverit.repositories.contracts.ShipmentRepository;
import com.deliverit.repositories.contracts.StatusRepository;
import com.deliverit.repositories.contracts.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShipmentMapper {


    private final ShipmentRepository shipmentRepository;
    private final StatusRepository statusRepository;


    @Autowired
    public ShipmentMapper(ShipmentRepository shipmentRepository,
                          StatusRepository statusRepository) {

        this.shipmentRepository = shipmentRepository;
        this.statusRepository = statusRepository;
    }


    public Shipment fromDto(ShipmentDto shipmentDto) {

        Shipment shipment = new Shipment();

        dtoToObject(shipmentDto, shipment);

        return shipment;
    }
    public ShipmentDto toDto(Shipment shipment) {
        ShipmentDto shipmentDto = new ShipmentDto();
        shipmentDto.setDepartureDate(shipment.getDepartureDate());
        shipmentDto.setArrivalDate(shipment.getArrivalDate());
        shipmentDto.setStatusId(shipment.getId());

        return shipmentDto;
    }
    public Shipment fromDto(int id, ShipmentDto shipmentDto) {

        Shipment shipment = shipmentRepository.getById(id);

        dtoToObject(shipmentDto, shipment);

        return shipment;
    }

    private void dtoToObject(ShipmentDto shipmentDto, Shipment shipment) {

        shipment.setArrivalDate(shipmentDto.getArrivalDate());

        shipment.setDepartureDate(shipmentDto.getDepartureDate());

        shipment.setStatus(statusRepository.getById(shipmentDto.getStatusId()));

    }
}
