package com.deliverit.repositories;

import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.City;
import com.deliverit.models.Parcel;
import com.deliverit.models.Shipment;
import com.deliverit.repositories.contracts.ShipmentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ShipmentRepositoryImpl implements ShipmentRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public ShipmentRepositoryImpl(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;

    }

    @Override
    public List<Shipment> getAll() {

        try (Session session = sessionFactory.openSession()) {

            Query<Shipment> query = session.createQuery("from Shipment", Shipment.class);

            return query.list();
        }

    }

    @Override
    public Shipment getById(int id) {

        try (Session session = sessionFactory.openSession()) {

            Shipment shipment = session.get(Shipment.class, id);

            if (shipment == null) {

                throw new EntityNotFoundException("Shipment", id);

            }
            return shipment;
        }
    }

    @Override
    public List<Shipment> getShipmentsByWarehouse(int warehouseId) {

        try (Session session = sessionFactory.openSession()) {

            Query<Shipment> query = session.createQuery("select distinct s " +
                    "from Shipment s " +
                    "join fetch s.parcels as parcel " +
                    "where parcel.warehouse.id = :id " +
                    "order by s.arrivalDate ", Shipment.class);

            query.setParameter("id", warehouseId);

            return query.list();
        }
    }

    @Override
    public List<Shipment> getShipmentToArrive() {

        try (Session session = sessionFactory.openSession()) {

            Query<Shipment> query = session.createQuery("from Shipment " +
                    "where status.name = 'on the way' " +
                    "order by arrivalDate asc ", Shipment.class);

            return query.list();
        }
    }

    public List<Shipment> getShipmentsByCustomer(int customerId) {

        try (Session session = sessionFactory.openSession()) {

            Query<Shipment> query = session.createQuery("select distinct s " +
                    "from Shipment s " +
                    "join fetch s.parcels as parcel " +
                    "where parcel.user.id = :customerId ", Shipment.class);

            query.setParameter("customerId", customerId);

            return query.list();
        }
    }

    public Shipment getShipmentsByParcel(int parcelId) {

        try (Session session = sessionFactory.openSession()) {

            Query<Shipment> query = session.createQuery("select s from Shipment s " +
                    "join s.parcels as parcel " +
                    "where parcel.id = :parcelId ", Shipment.class);

            query.setParameter("parcelId", parcelId);

            List<Shipment> list = query.list();

            if (list.size() == 0) {
                throw new EntityNotFoundException("Shipment", "parcel", String.valueOf(parcelId));
            }

            return list.get(0);
        }

    }


    @Override
    public Shipment create(Shipment shipment) {

        try (Session session = sessionFactory.openSession()) {

            session.save(shipment);

            return shipment;
        }
    }

    @Override
    public Shipment update(Shipment shipment) {

        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            session.update(shipment);

            session.getTransaction().commit();

        }
        return shipment;

    }

    @Override
    public Shipment delete(int id) {

        Shipment shipmentToDelete = getById(id);

        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            session.delete(shipmentToDelete);

            session.getTransaction().commit();
        }
        return shipmentToDelete;
    }
}
