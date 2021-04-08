package com.deliverit.repositories;

import com.deliverit.models.Country;
import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.repositories.contracts.CountryRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CountryRepositoryImpl implements CountryRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CountryRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Country> getAll() {

        try (Session session = sessionFactory.openSession()) {

            Query<Country> query = session.createQuery("from Country", Country.class);

            return query.list();
        }
    }

    public Country getById(int id) {

        try (Session session = sessionFactory.openSession()) {

            Country country = session.get(Country.class, id);

            if (country == null) {

                throw new EntityNotFoundException("Country", id);

            }
            return country;
        }
    }
}
