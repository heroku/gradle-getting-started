package com.deliverit.repositories;

import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.City;
import com.deliverit.repositories.contracts.CityRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CityRepositoryImpl implements CityRepository {


    private final SessionFactory sessionFactory;

    @Autowired
    public CityRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public List<City> getAll() {

        try (Session session = sessionFactory.openSession()) {

            Query<City> query = session.createQuery("from City order by name", City.class);

            return query.list();
        }

    }

    public City getById(int id) {

        try (Session session = sessionFactory.openSession()) {

            City city = session.get(City.class, id);

            if (city == null) {

                throw new EntityNotFoundException("City", id);

            }
            return city;
        }
    }
}
