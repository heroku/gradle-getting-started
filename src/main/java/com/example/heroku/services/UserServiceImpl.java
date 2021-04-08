package com.deliverit.services;

import com.deliverit.exceptions.DuplicateEntityException;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.exceptions.IllegalDeleteException;
import com.deliverit.exceptions.UnauthorizedOperationException;
import com.deliverit.models.*;
import com.deliverit.repositories.contracts.RoleRepository;
import com.deliverit.repositories.contracts.UserRepository;
import com.deliverit.services.contracts.AddressService;
import com.deliverit.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.deliverit.services.AuthorizationHelper.verifyUserIsAuthorized;
import static com.deliverit.services.AuthorizationHelper.verifyUserIsCustomerOrEmployee;

@Service
public class UserServiceImpl implements UserService {


    private static final String USER = "User";
    private static final String UPDATING_PRIVATE_INFO_ERROR_MESSAGE = "Not authorized to update other customer.";
    private static final String DELETING_PRIVATE_INFO_ERROR_MESSAGE = "Not authorized to delete other customer.";
    private static final String NOT_AUTHORIZED_TO_SEE_OTHER_CUSTOMER_PARCELS = "Not authorized to see other customer parcels.";
    private static final String USER_ALREADY_HAVE_THIS_ROLE = "User with email: %s already have this role.";
    private static final String DELETE_ERROR_MESSAGE = "User with id: %d has parcels and cannot be deleted.";
    private static final String EMPLOYEE_CANNOT_DELETE_ANOTHER_EMPLOYEE = "Employee cannot delete another employee";
    private final UserRepository userRepository;
    private final AddressService addressService;
    private final RoleRepository roleRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, AddressService addressService,
                           RoleRepository roleRepository) {

        this.userRepository = userRepository;
        this.addressService = addressService;
        this.roleRepository = roleRepository;

    }

    @Override
    public Long getCustomersCount() {
        return userRepository.getCustomersCount();
    }

    @Override
    public Shipment getStatusOfParcel(int customerId, int parcelId, User user) {

        verifyUserIsCustomerOrEmployee(user);

        if (user.getId() != customerId && !user.isEmployee()) {

            throw new UnauthorizedOperationException(NOT_AUTHORIZED_TO_SEE_OTHER_CUSTOMER_PARCELS);

        }
        return userRepository.getStatusOfParcel(parcelId);
    }


    public List<User> getAll(User user) {

        verifyUserIsAuthorized(user);

        return userRepository.getAll();

    }

    @Override
    public List<User> getAllCustomers() {
        return userRepository.getAllCustomers();
    }


    public User getById(User user, int id) {

        if (!user.isEmployee() && user.getId() != id){
            throw new UnauthorizedOperationException("Customer can edit his own account");
        }

        return userRepository.getById(id);

    }

    @Override
    public User getByEmail(String email) {

        return userRepository.getByEmail(email);
    }

    @Override
    public void addRoleToUserRoles(int userId, int roleId, User user) {

        verifyUserIsAuthorized(user);

        User userToAddRole = userRepository.getById(userId);

        Role role = roleRepository.getById(roleId);

        Set<Role> roles = userToAddRole.getRoles();

        if (roles.contains(role)) {

            throw new DuplicateEntityException(String.format(USER_ALREADY_HAVE_THIS_ROLE,
                    userToAddRole.getEmail()));

        }

        roles.add(role);

        userToAddRole.setRoles(roles);

        userRepository.update(userToAddRole);

    }

    public void addRoleToRegisteredUser(User user) {


        User userToAddRole = userRepository.getById(user.getId());

        Role role = roleRepository.getById(1);

        Set<Role> roles = userToAddRole.getRoles();

        if (roles.contains(role)) {

            throw new DuplicateEntityException(String.format(USER_ALREADY_HAVE_THIS_ROLE,
                    userToAddRole.getEmail()));

        }

        roles.add(role);

        userToAddRole.setRoles(roles);

        userRepository.update(userToAddRole);

    }


    @Override
    public List<User> filter(User user, Optional<String> email, Optional<String> firstName,
                             Optional<String> lastName, Optional<String> word) {

        verifyUserIsAuthorized(user);

        if (word.isPresent()) {

            return userRepository.filterByOneWord(word);

        }
        return userRepository.filter(email, firstName, lastName);
    }

    @Override
    public List<Parcel> getIncomingCustomerParcels(int id, User user) {

        verifyUserIsCustomerOrEmployee(user);

        if (user.getId() != id && !user.isEmployee()) {

            throw new UnauthorizedOperationException(NOT_AUTHORIZED_TO_SEE_OTHER_CUSTOMER_PARCELS);

        }

        return userRepository.getIncomingCustomerParcels(id);
    }

    @Override
    public User create(User user) {

        checkIfEmailExist(user);

        setAddressToUser(user);

        User userToCreate = userRepository.create(user);

        addRoleToRegisteredUser(user);

        return userToCreate;
    }


    @Override
    public User update(User userToUpdate, User user) {

        verifyUserIsCustomerOrEmployee(user);

        if (userToUpdate.getId() != user.getId() && !user.isEmployee()) {

            throw new UnauthorizedOperationException(UPDATING_PRIVATE_INFO_ERROR_MESSAGE);

        }

        checkIfEmailExist(userToUpdate);

        setAddressToUser(userToUpdate);

        return userRepository.update(userToUpdate);
    }

    @Override
    public User delete(User user, int id) {

        verifyUserIsCustomerOrEmployee(user);


        User userToDelete;

        try {

            userToDelete = userRepository.getById(id);

            if (userToDelete.isEmployee() && user.getId() != userToDelete.getId()){
                throw new IllegalDeleteException(EMPLOYEE_CANNOT_DELETE_ANOTHER_EMPLOYEE);
            }

        } catch (EntityNotFoundException e) {

            throw new EntityNotFoundException(USER, id);

        }

        if (userToDelete.getId() != user.getId() && !user.isEmployee()) {

            throw new UnauthorizedOperationException(DELETING_PRIVATE_INFO_ERROR_MESSAGE);

        }

        List<Parcel> parcels = userRepository.getCustomerParcels(userToDelete.getId());


        if (!parcels.isEmpty()) {

            throw new IllegalDeleteException(String.format(DELETE_ERROR_MESSAGE,
                    userToDelete.getId()));

        }

        return userRepository.delete(id);
    }


    private void checkIfEmailExist(User user) {

        boolean isEmailTaken = true;

        User userToCompare;
        try {

            userToCompare = userRepository.getByEmail(user.getEmail());

            if (userToCompare.getId() == user.getId()){

                isEmailTaken = false;

            }

        } catch (EntityNotFoundException e) {

            isEmailTaken = false;

        }

        if (isEmailTaken) {

            throw new DuplicateEntityException(USER, "email", user.getEmail());

        }

    }

    private void setAddressToUser(User user) {

        user.setAddress(addressService.getOrCreate(user.getAddress()));

    }

}
