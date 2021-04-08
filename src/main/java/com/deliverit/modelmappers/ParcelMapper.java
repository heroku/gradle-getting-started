package com.deliverit.modelmappers;

import com.deliverit.models.Parcel;
import com.deliverit.models.Shipment;
import com.deliverit.models.dto.ParcelDto;
import com.deliverit.models.dto.ParcelTransferDto;
import com.deliverit.repositories.contracts.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class ParcelMapper {


    private final ParcelRepository parcelRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final WarehouseRepository warehouseRepository;

    @Autowired
    public ParcelMapper(ParcelRepository parcelRepository,
                        CategoryRepository categoryRepository,
                        UserRepository userRepository,
                        WarehouseRepository warehouseRepository) {

        this.parcelRepository = parcelRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.warehouseRepository = warehouseRepository;

    }

    public Parcel fromDto(ParcelDto parcelDto) {

        Parcel parcel = new Parcel();

        dtoToObject(parcelDto, parcel);

        return parcel;
    }

    public Parcel fromDto(int id, ParcelDto parcelDto) {

        Parcel parcel = parcelRepository.getById(id);

        dtoToObject(parcelDto, parcel);

        return parcel;
    }

    public ParcelDto toDto(Parcel parcel) {
        ParcelDto parcelDto = new ParcelDto();
        parcelDto.setWeight(parcel.getWeight());
        parcelDto.setCategoryId(parcel.getCategory().getId());
        parcelDto.setUserId(parcel.getUser().getId());
        parcelDto.setWarehouseId(parcel.getWarehouse().getId());
        return parcelDto;
    }

    private void dtoToObject(ParcelDto parcelDto, Parcel parcel) {

        parcel.setWeight(parcelDto.getWeight());

        parcel.setCategory(categoryRepository.getById(parcelDto.getCategoryId()));

        parcel.setUser(userRepository.getById(parcelDto.getUserId()));

        parcel.setWarehouse(warehouseRepository.getById(parcelDto.getWarehouseId()));

    }

    public List<ParcelTransferDto> convertAll(List<Shipment> shipmentList,List<Parcel> parcelList){

        List<ParcelTransferDto> parcelTransferDtos = new ArrayList<>();


        for (int i = 0; i < shipmentList.size(); i++) {

            for (int j = 0; j < parcelList.size(); j++) {

                if (shipmentList.get(i).getParcels().contains(parcelList.get(j))){

                    convertToTransferDto(shipmentList,parcelList,parcelTransferDtos,i,j);

                    j = j - 1;
                }
            }
        }
        int lastParcelInAList = 0;

        while (parcelList.size() > 0) {

            convertToTransferDto(shipmentList,parcelList,parcelTransferDtos,-1,lastParcelInAList);


        }

        return parcelTransferDtos;
    }

    private void convertToTransferDto(List<Shipment> shipmentList,
                                                         List<Parcel> parcelList,
                                                         List<ParcelTransferDto> parcelTransferDtos,
                                                         int i,
                                                         int j){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        ParcelTransferDto dto = new ParcelTransferDto();
        dto.setId(parcelList.get(j).getId());
        if (i == -1) {
            dto.setDepartureDate("n/a");
            dto.setArrivalDate("n/a");
            dto.setStatus("n/a");
        }else {
            dto.setDepartureDate(dateFormat.format(shipmentList.get(i).getDepartureDate()));
            dto.setArrivalDate(dateFormat.format(shipmentList.get(i).getArrivalDate()));
            dto.setStatus(shipmentList.get(i).getStatus().getName());
        }
        dto.setCategory(parcelList.get(j).getCategory().getName());
        dto.setUser(parcelList.get(j).getUser().getEmail());
        dto.setWeight(parcelList.get(j).getWeight());
        dto.setStreetName(parcelList.get(j).getWarehouse().getAddress().getStreetName());
        dto.setCity(parcelList.get(j).getWarehouse().getAddress().getCity().getName());
        dto.setCountry(parcelList.get(j).getWarehouse().getAddress().getCity().getCountry().getName());
        parcelTransferDtos.add(dto);
        parcelList.remove(parcelList.get(j));

    }
}
