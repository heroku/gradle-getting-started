package com.deliverit.repositories;

import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.City;
import com.deliverit.models.Status;
import com.deliverit.models.User;
import com.deliverit.repositories.contracts.StatusRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StatusRepositoryImpl implements StatusRepository {


    private final SessionFactory sessionFactory;

    @Autowired
    public StatusRepositoryImpl(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;

    }

    @Override
    public Status getById(int id) {

        try (Session session = sessionFactory.openSession()) {

            Status status = session.get(Status.class, id);

            if (status == null) {

                throw new EntityNotFoundException("Status", id);
            }
            return status;
        }
    }

    @Override
    public List<Status> getALl() {
        try (Session session = sessionFactory.openSession()) {

            Query<Status> query = session.createQuery("from Status ", Status.class);

            return query.list();
        }
    }
    }

