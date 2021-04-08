package com.deliverit.repositories;

import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Address;
import com.deliverit.repositories.contracts.AddressRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AddressRepositoryImpl implements AddressRepository {
    private final SessionFactory sessionFactory;


    @Autowired
    public AddressRepositoryImpl(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;

    }

    @Override
    public Address getById(int id) {
        try (Session session = sessionFactory.openSession()) {

            Address address = session.get(Address.class, id);

            if (address == null) {
                throw new EntityNotFoundException("Address", id);
            }
            return address;
        }
    }

    public Address create(Address address) {

        try (Session session = sessionFactory.openSession()) {

            session.save(address);

            return getById(address.getId());
        }
    }

    public Address getByAddressAndCity(String address, int cityId) {

        try (Session session = sessionFactory.openSession()) {

            Query<Address> query = session.createQuery("from Address a where a.streetName = :address " +
                    "and a.city.id = :cityId ", Address.class);

            query.setParameter("address", address);

            query.setParameter("cityId", cityId);

            List<Address> result = query.list();

            if (result.size() == 0) {

                throw new EntityNotFoundException("Address", "address", address);

            }
            return result.get(0);
        }
    }

    @Override
    public List<Address> getAll() {
        try (Session session = sessionFactory.openSession()) {

            Query<Address> query = session.createQuery("from Address ", Address.class);


            return query.list();
        }
    }
}
