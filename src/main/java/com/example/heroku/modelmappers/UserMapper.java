package com.deliverit.modelmappers;

import com.deliverit.models.Address;
import com.deliverit.models.City;
import com.deliverit.models.Role;
import com.deliverit.models.User;
import com.deliverit.models.dto.*;
import com.deliverit.repositories.contracts.RoleRepository;
import com.deliverit.repositories.contracts.UserRepository;
import com.deliverit.services.contracts.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {


    private final UserRepository userRepository;
    private final CityService cityService;
    private final RoleRepository roleRepository;


    @Autowired
    public UserMapper(UserRepository userRepository, CityService cityService, RoleRepository roleRepository) {

        this.userRepository = userRepository;
        this.cityService = cityService;

        this.roleRepository = roleRepository;
    }

    public User fromDto(UserDto userDto) {

        User user = new User();

        dtoToObject(userDto, user);

        return user;
    }

    public User fromDto(int id, UserDto userDto) {

        User user = userRepository.getById(id);

        dtoToObject(userDto, user);

        return user;
    }

    public User fromDto(RegisterDto registerDto) {

        Address address = new Address();
        address.setStreetName(registerDto.getStreetName());
        address.setCity(cityService.getById(registerDto.getCityId()));




        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(registerDto.getPassword());
        user.setAddress(address);

        return user;
    }

    public UserTransferDataDto toDto(User user) {
        UserTransferDataDto userDto = new UserTransferDataDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setAddress(user.getAddress());
        userDto.setRoles(user.getRoles().stream().map(Role::getRole).collect(Collectors.toSet()));
        return userDto;
    }

    public Role fromName(String roleName) {
        return roleRepository.getByName(roleName);
    }


    public User setNewRolesFromDto(UserTransferDataDto userTransferDataDto,int id){
        User user = userRepository.getById(id);

        user.setRoles(userTransferDataDto.getRoles().stream().map(this::fromName).collect(Collectors.toSet()));
        return user;
    }

    public User editPasswordFromDto(int id, EditUserPasswordDto editUserPasswordDto) {

        User user = userRepository.getById(id);

        user.setPassword(editUserPasswordDto.getPassword());

        return user;
    }

    public EditUserPasswordDto editPasswordFromUser(User user) {
        EditUserPasswordDto dto = new EditUserPasswordDto();
        dto.setId(user.getId());
        dto.setPassword(user.getPassword());
        return dto;
    }

    public User editAddressFromAddressDto(int id, AddressDto addressDto) {
        Address address = new Address();
        address.setStreetName(addressDto.getStreetName());
        address.setCity(cityService.getById(addressDto.getCityId()));

        User userToUpdate = userRepository.getById(id);
        userToUpdate.setAddress(address);
        return userToUpdate;
    }

    private void dtoToObject(UserDto userDto, User user) {

        City city = cityService.getById(userDto.getAddress().getCityId());

        Address address = new Address();

        address.setStreetName(userDto.getAddress().getStreetName());

        address.setCity(city);

        user.setFirstName(userDto.getFirstName());

        user.setLastName(userDto.getLastName());

        user.setEmail(userDto.getEmail());

        user.setAddress(address);

    }
}
