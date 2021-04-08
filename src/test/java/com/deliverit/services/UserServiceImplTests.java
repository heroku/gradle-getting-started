package com.deliverit.services;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.IllegalDeleteException;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.*;
import com.deliverit.repositories.contracts.RoleRepository;
import com.deliverit.repositories.contracts.UserRepository;
import com.deliverit.services.contracts.AddressService;
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
public class UserServiceImplTests {

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    AddressService addressService;

    @InjectMocks
    UserServiceImpl userService;


    @Test
    public void getAll_Should_ThrowException_When_UserIsNotEmployee() {

        User user = createMockUser();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.getAll(user));

    }

    @Test
    public void getAll_Should_ReturnAllUsers_When_UsersExist() {

        User user = createMockUser();

        User user1 = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        List<User> usersList = new ArrayList<>();

        usersList.add(user1);

        usersList.add(user);


        Mockito.when(userService.getAll(user))
                .thenReturn(usersList);

        userService.getAll(user);

        Mockito.verify(userRepository, Mockito.times(1))
                .getAll();
    }

    @Test
    public void getById_Should_ThrowException_When_UserIsNotEmployee() {

        User user = createMockUser();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.getById(user, 1));

    }


    @Test
    public void getById_Should_ReturnById_When_UserIsEmployee() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Mockito.when(userService.getById(user, 1))
                .thenReturn(user);

        userService.getById(user, user.getId());

        Mockito.verify(userRepository, Mockito.times(1))
                .getById(user.getId());

    }


    @Test
    public void getByEmail_Should_ReturnUser_When_EmailIsValid() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Mockito.when(userRepository.getByEmail("User.email")).thenReturn(user);

        userService.getByEmail("User.email");

        Mockito.verify(userRepository, Mockito.times(1))
                .getByEmail("User.email");

    }


    @Test
    public void AddToUserRoles_Should_Throw_WhenUserIsNotEmployee() {

        User user = createMockUser();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.addRoleToUserRoles(user.getId(), 2, user));
    }

    @Test
    public void filter_Should_ThrowException_When_UserIsNotEmployee() {

        User user = createMockUser();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.filter(user, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));

    }

    @Test
    public void addToUserRoles_Should_Throw_WhenUserIsNotFound() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Role role = new Role(1, "Customer");

        Mockito.when(userRepository.getById(5)).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.addRoleToUserRoles(5, role.getId(), user));


    }

    @Test
    public void addRoleToUserRoles_Should_ThrowException_When_RoleIsNotFound() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Role role = new Role(1, "Customer");

        Mockito.when(userRepository.getById(5)).thenReturn(user);

        Mockito.when(roleRepository.getById(1)).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.addRoleToUserRoles(5, role.getId(), user));

    }


    @Test
    public void addRoleToUserRoles_Should_AddROle_When_UserAndRoleExist() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        User userToAddRole = createMockUser();

        Role role = new Role(3, "Employee");

        Set<Role> roles = new HashSet<>();

        userToAddRole.setRoles(roles);

        Mockito.when(userRepository.getById(2)).thenReturn(userToAddRole);

        Mockito.when(roleRepository.getById(role.getId())).thenReturn(role);

        userService.addRoleToUserRoles(2, role.getId(), user);

        Mockito.verify(userRepository, Mockito.times(1)).update(user);
    }

    @Test
    public void getRoleById_ShouldThrowWhenRoleNotFound() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Mockito.when(roleRepository.getById(2)).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> roleRepository.getById(2));
    }


    @Test
    public void addRoleToUser_Should_Throw_WhenDuplicateRoles() {

        User user = createMockUser();

        User userToSetRole = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Role role = new Role(1, "Customer");

        Set<Role> set = Set.of(role);

        userToSetRole.setRoles(set);

        Mockito.when(roleRepository.getById(role.getId())).thenReturn(role);

        Mockito.when(userRepository.getById(userToSetRole.getId())).thenReturn(userToSetRole);

        Assertions.assertThrows(DuplicateEntityException.class,
                () -> userService.addRoleToUserRoles(userToSetRole.getId(), role.getId(), user));
    }


    @Test
    public void filter_Should_Throw_WhenUserIsNotEmployee() {

        User user = createMockUser();


        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.filter(user, Optional.empty(),
                        Optional.of("Ivancho"), Optional.of("cloth"), Optional.empty()));


    }

    @Test
    public void filter_Should_Filter_WhenUserIsEmployee() {

        Parcel parcel = createMockParcel();

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        List<User> result = new ArrayList<>();

        result.add(user);

        Mockito.when(userRepository.filter(Optional.of("Ivancho"), Optional.empty(), Optional.empty()))
                .thenReturn(result);

        userService.filter(user, Optional.of("Ivancho"), Optional.empty(), Optional.empty(), Optional.empty());

        Mockito.verify(userRepository, Mockito.times(1))
                .filter(Optional.of("Ivancho"), Optional.empty(), Optional.empty());

    }

    @Test
    public void getCustomersParcels_Should_Throw_WhenUserIsNotEmployeeOrCustomer() {

        User user = createMockUser();

        user.setRoles(Set.of(new Role(5, "prodavach")));

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.getIncomingCustomerParcels(5, user));

    }

    @Test
    public void getStatusOfParcels_Should_Throw_WhenUserIsNotEmployeeOrCustomer() {

        User user = createMockUser();

        Parcel parcel = createMockParcel();

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.getStatusOfParcel(2, parcel.getId(), user));


    }

    @Test
    public void getStatusOfParcels_Should_Throw_WhenParcelNotFound() {


        Mockito.when(userRepository.getStatusOfParcel(2))
                .thenThrow(EntityNotFoundException.class);


        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userRepository.getStatusOfParcel(2));

    }


    @Test
    public void getCustomersParcels_Should_ReturnWhenUserIsOk() {

        User user = createMockUser();

        Parcel parcel = createMockParcel();

        parcel.setUser(user);

        List<Parcel> parcelList = new ArrayList<>();
        parcelList.add(parcel);

        userService.getIncomingCustomerParcels(user.getId(), user);

        Mockito.verify(userRepository, Mockito.times(1))
                .getIncomingCustomerParcels(user.getId());

    }

    @Test
    public void getCustomersIncomingParcels_Should_Throw_WhenUserIsNotAuthorized() {

        User user = createMockUser();

        User fakeUser = createMockUser();

        Mockito.when(userService.getIncomingCustomerParcels(user.getId(), fakeUser))
                .thenThrow(UnauthorizedOperationException.class);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.getIncomingCustomerParcels(user.getId(), fakeUser));


    }

    @Test
    public void getCustomersCount_ShouldReturnCount() {
        User user = createMockUser();

        User user1 = createMockUser();

        List<User> users = new ArrayList<>();

        users.add(user);

        users.add(user1);

        Mockito.when(userRepository.getCustomersCount()).thenReturn((long) users.size());

        userService.getCustomersCount();

        Mockito.verify(userRepository, Mockito.times(1))
                .getCustomersCount();
    }

    @Test
    public void create_Should_Throw_WhenUserExists() {

        User user = createMockUser();

        Assertions.assertThrows(DuplicateEntityException.class,
                () -> userService.create(user));

    }

    @Test
    public void create_Should_Create_When_NotExistUserWithSameEmail() {

        User user = createMockUser();

        Address address = createMockAddress();

        user.setAddress(address);

        Mockito.when(userRepository.getByEmail(user.getEmail()))
                .thenThrow(EntityNotFoundException.class);

        Mockito.when(addressService.getOrCreate(address))
                .thenReturn(address);

        userService.create(user);

        Mockito.verify(userRepository, Mockito.times(1)).create(user);

    }


    @Test
    public void update_Should_ThrowException_When_UserIsNotAuthorized() {

        User userToUpdate = createMockUser();

        User user = createMockUser();

        user.setId(5);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.update(userToUpdate, user));

    }


    @Test
    public void update_Should_UpdateUser_When_Exist() {

        User userToUpdate = createMockUser();

        User user = createMockUser();

        user.setRoles(Set.of(new Role(2, "Employee")));

        Address address = createMockAddress();

        userToUpdate.setAddress(address);

        Mockito.when(userRepository.update(userToUpdate))
                .thenReturn(userToUpdate);

        Mockito.when(userRepository.getByEmail(userToUpdate.getEmail()))
                .thenThrow(EntityNotFoundException.class);

        Mockito.when(addressService.getOrCreate(address))
                .thenReturn(address);

        userService.update(userToUpdate, user);

        Mockito.verify(userRepository, Mockito.times(1))
                .update(userToUpdate);

    }

    @Test
    public void delete_Should_Throw_WhenUserIsNotFound() {

        User user = createMockUser();

        Mockito.when(userRepository.getById(user.getId()))
                .thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.delete(user, user.getId()));

    }

    @Test
    public void delete_Should_Throw_WhenUserIsAuthorized() {


        User user1 = createMockUser();


        Mockito.when(userRepository.getById(user1.getId()))
                .thenThrow(UnauthorizedOperationException.class);

        Assertions.assertThrows(UnauthorizedOperationException.class,
                () -> userService.delete(user1, user1.getId()));

    }

    @Test
    public void delete_Should_Throw_WhenUserHasParcels() {

        User user1 = createMockUser();

        User fakeUser = createMockUser();

        fakeUser.setId(10);

        Parcel parcel = createMockParcel();

        parcel.setUser(fakeUser);

        user1.setRoles(Set.of(new Role(2, "Employee")));

        List<Parcel> parcelList = new ArrayList<>();
        parcelList.add(parcel);

        Mockito.when(userRepository.getById(fakeUser.getId()))
                .thenReturn(fakeUser);

        Mockito.when(userRepository.getCustomerParcels(fakeUser.getId()))
                .thenReturn(parcelList);

        Assertions.assertThrows(IllegalDeleteException.class,
                () -> userService.delete(user1, fakeUser.getId()));

    }


    @Test
    public void delete_Should_Delete_WhenUserExists() {

        User user1 = createMockUser();

        User user = createMockUser();

        List<Parcel> parcels = new ArrayList<>();

        user1.setRoles(Set.of(new Role(2, "Employee")));

        Mockito.when(userRepository.getById(user.getId()))
                .thenReturn(user);

        Mockito.when(userRepository.getCustomerParcels(user.getId()))
                .thenReturn(parcels);

        userService.delete(user1, user.getId());

        Mockito.verify(userRepository, Mockito.times(1))
                .delete(user.getId());

    }
}
