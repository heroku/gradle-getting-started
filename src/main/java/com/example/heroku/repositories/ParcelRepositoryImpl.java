package com.deliverit.repositories;

import com.deliverit.models.Parcel;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.User;
import com.deliverit.repositories.contracts.ParcelRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ParcelRepositoryImpl implements ParcelRepository {


    private final SessionFactory sessionFactory;

    @Autowired
    public ParcelRepositoryImpl(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;

    }

    @Override
    public List<Parcel> getAll() {

        try (Session session = sessionFactory.openSession()) {

            Query<Parcel> query = session.createQuery("from Parcel", Parcel.class);

            return query.list();
        }

    }


    @Override
    public Parcel getById(int id) {

        try (Session session = sessionFactory.openSession()) {

            Parcel parcel = session.get(Parcel.class, id);

            if (parcel == null) {

                throw new EntityNotFoundException("Parcel", id);
            }
            return parcel;
        }
    }

    @Override
    public List<Parcel> sort(Optional<String> weight, Optional<String> date) {

        String queryString = "select p from Shipment s join s.parcels as p order by ";

        if (weight.isPresent()){
            queryString += "p.weight " + weight.get();
        }

        if (weight.isPresent() && date.isPresent()) {

            queryString += " , s.arrivalDate " + date.get();
        }
        if (weight.isEmpty() && date.isPresent()){
            queryString += " s.arrivalDate " + date.get();
        }

        try (Session session = sessionFactory.openSession()) {

            Query<Parcel> query = session.createQuery(queryString, Parcel.class);

            return query.list();

        }
    }


    @Override
    public List<Parcel> filterByCategory(int id) {

        try (Session session = sessionFactory.openSession()) {

            Query<Parcel> query = session.createQuery("from Parcel " +
                    "where category.id = :id ", Parcel.class);

            query.setParameter("id", id);
        return query.list();
        }
    }

    @Override
    public List<Parcel> filterByCustomer(int id) {

        try (Session session = sessionFactory.openSession()) {

            Query<Parcel> query = session.createQuery("from Parcel " +
                    "where user.id = :id ", Parcel.class);

            query.setParameter("id", id);

            return query.list();
        }
    }

    @Override
    public List<Parcel> filterByWarehouse(int id) {

        try (Session session = sessionFactory.openSession()) {

            Query<Parcel> query = session.createQuery("from Parcel " +
                    "where warehouse.id = :id ", Parcel.class);

            query.setParameter("id", id);

            return query.list();
        }
    }

    @Override
    public List<Parcel> getParcelsByCustomers(User user) {

        try (Session session = sessionFactory.openSession()) {

            Query<Parcel> query = session.createQuery("from Parcel " +
                    "where user.id = :id ", Parcel.class);

            query.setParameter("id", user.getId());

            return query.list();
        }
    }

    @Override
    public List<Parcel> filter(Optional<Double> weight,
                               Optional<String> customer,
                               Optional<String> warehouse,
                               Optional<String> category) {

        try (Session session = sessionFactory.openSession()) {

            Query<Parcel> query = session.createQuery("from Parcel p " +
                    "where p.weight >= :weight " +
                    "and p.user.email like concat('%',:customer,'%') " +
                    "and p.warehouse.address.streetName like concat('%',:warehouse,'%') " +
                    "and p.category.name like concat('%',:category,'%') ", Parcel.class);

            query.setParameter("weight", weight.orElse(0.0));

            query.setParameter("customer", customer.orElse(""));

            query.setParameter("warehouse", warehouse.orElse(""));

            query.setParameter("category", category.orElse(""));

            return query.list();
        }
    }

    @Override
    public Parcel create(Parcel parcel) {

        try (Session session = sessionFactory.openSession()) {

            session.save(parcel);

            return parcel;
        }
    }

    @Override
    public Parcel update(Parcel parcel) {

        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            session.update(parcel);

            session.getTransaction().commit();

        }
        return parcel;
    }

    @Override
    public Parcel delete(int id) {

        Parcel parcelToDelete = getById(id);

        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            session.delete(parcelToDelete);

            session.getTransaction().commit();

        }
        return parcelToDelete;
    }
}
