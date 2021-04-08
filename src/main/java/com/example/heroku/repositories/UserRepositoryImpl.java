package com.deliverit.repositories;

import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Shipment;
import com.deliverit.models.User;
import com.deliverit.models.Parcel;
import com.deliverit.repositories.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {


    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Long getCustomersCount() {

        try (Session session = sessionFactory.openSession()) {

            Query query = session.createQuery("select count(*) from User u " +
                    "join u.roles as role " +
                    "where role.role = 'Customer' ", Long.class);

            return (Long) query.uniqueResult();
        }
    }

    @Override
    public List<User> getAll() {
        try (Session session = sessionFactory.openSession()) {

            Query<User> query = session.createQuery("from User ", User.class);

            return query.list();
        }
    }

    @Override
    public User getById(int id) {

        try (Session session = sessionFactory.openSession()) {

            User user = session.get(User.class, id);

            if (user == null) {

                throw new EntityNotFoundException("User", id);

            }
            return user;
        }
    }

    public User getByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {

            Query<User> query = session.createQuery("from User " +
                    "where email = :email ", User.class);

            query.setParameter("email", email);

            List<User> result = query.list();

            if (result.size() == 0) {

                throw new EntityNotFoundException("User", "email", email);

            }
            return result.get(0);
        }
    }


    @Override
    public List<User> filter(Optional<String> email, Optional<String> firstName, Optional<String> lastName) {

        try (Session session = sessionFactory.openSession()) {

            Query<User> query = session.createQuery("from User " +
                    "where email like concat('%', :email,'%') " +
                    "and firstName like concat('%', :firstName,'%') " +
                    "and lastName like concat('%', :lastName,'%')", User.class);

            query.setParameter("email", email.orElse(""));

            query.setParameter("firstName", firstName.orElse(""));

            query.setParameter("lastName", lastName.orElse(""));

            return query.list();
        }

    }

    @Override
    public List<Parcel> getIncomingCustomerParcels(int id) {

        try (Session session = sessionFactory.openSession()) {

            Query<Parcel> query = session.createQuery("SELECT p from Shipment s " +
                    "join s.parcels as p " +
                    "where p.user.id = :id " +
                    "and s.status.name = 'on the way' ", Parcel.class);

            query.setParameter("id", id);

            return query.list();
        }
    }

    public List<Parcel> getCustomerParcels(int id) {

        try (Session session = sessionFactory.openSession()) {

            Query<Parcel> query = session.createQuery("from Parcel " +
                    "where user.id = :id", Parcel.class);

            query.setParameter("id", id);

            return query.list();
        }
    }

    @Override
    public List<User> filterByOneWord(Optional<String> word) {

        try (Session session = sessionFactory.openSession()) {

            Query<User> query = session.createQuery("from User " +
                    "where email like concat('%', :word,'%') " +
                    "or firstName like concat('%', :word,'%') " +
                    "or lastName like concat('%', :word,'%')", User.class);

            query.setParameter("word", word.orElse(""));


            return query.list();
        }
    }

    @Override
    public Shipment getStatusOfParcel(int parcelId) {

        try (Session session = sessionFactory.openSession()) {

            Query<Shipment> query = session.createQuery("Select s from Shipment s " +
                    "join s.parcels as p " +
                    "where p.id = :parcelId ", Shipment.class);

            query.setParameter("parcelId", parcelId);

            List<Shipment> shipmentList = query.list();

            if (shipmentList.size() == 0) {

                throw new EntityNotFoundException("Parcel", "id", String.valueOf(parcelId));

            }
            return shipmentList.get(0);
        }
    }

    @Override
    public List<User> getAllCustomers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select u from User u join u.roles as r " +
                    "where r.role = 'Customer'",User.class).list();
        }
    }

    @Override
    public User create(User user) {

        try (Session session = sessionFactory.openSession()) {

            session.save(user);

            return getByEmail(user.getEmail());
        }
    }

    @Override
    public User update(User user) {

        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            session.update(user);

            session.getTransaction().commit();
        }
        return user;
    }

    @Override
    public User delete(int id) {

        User userToDelete = getById(id);

        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            session.delete(userToDelete);

            session.getTransaction().commit();

        }
        return userToDelete;
    }
}
