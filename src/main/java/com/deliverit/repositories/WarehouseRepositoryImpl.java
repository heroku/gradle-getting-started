package com.deliverit.repositories;

import com.deliverit.models.Warehouse;
import com.deliverit.repositories.contracts.WarehouseRepository;
import com.deliverit.exceptions.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Repository
public class WarehouseRepositoryImpl implements WarehouseRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public WarehouseRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Warehouse> getAll(Optional<String> address, Optional<String> city, Optional<String> country) {

        try (Session session = sessionFactory.openSession()) {

            Query<Warehouse> query = session.createQuery("from Warehouse w " +
                    "where w.address.streetName like concat('%',:address,'%') " +
                    "and w.address.city.name like concat('%',:city,'%') " +
                    "and w.address.city.country.name like concat('%', :country, '%') ", Warehouse.class);

            query.setParameter("address", address.orElse(""));

            query.setParameter("city", city.orElse(""));

            query.setParameter("country", country.orElse(""));

            return query.list();

        }
    }

    @Override
    public Warehouse getById(int id) {

        try (Session session = sessionFactory.openSession()) {

            Warehouse warehouse = session.get(Warehouse.class, id);

            if (warehouse == null) {

                throw new EntityNotFoundException("Warehouse", id);

            }
            return warehouse;
        }
    }

    public Warehouse getByAddressId(Warehouse warehouse) {

        try (Session session = sessionFactory.openSession()) {

            Query<Warehouse> query = session.createQuery("from Warehouse w " +
                    "where w.address.id = :id", Warehouse.class);

            query.setParameter("id", warehouse.getAddress().getId());

            List<Warehouse> result = query.list();

            if (result.size() == 0) {

                throw new EntityNotFoundException("Warehouse", "address", warehouse.getAddress().getStreetName());
            }

            return result.get(0);
        }

    }


    @Override
    public Warehouse create(Warehouse warehouse) {

        try (Session session = sessionFactory.openSession()) {

            session.save(warehouse);

            return warehouse;
        }
    }

    @Override
    public Warehouse update(Warehouse warehouse) {

        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            session.update(warehouse);

            session.getTransaction().commit();
        }

        return warehouse;
    }

    @Override
    public Warehouse delete(int id) {

        Warehouse warehouseToDelete = getById(id);

        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            session.delete(warehouseToDelete);

            session.getTransaction().commit();
        }

        return warehouseToDelete;

    }

}
