package com.deliverit.repositories;

import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Role;
import com.deliverit.repositories.contracts.RoleRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public RoleRepositoryImpl(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;

    }

    @Override
    public Role getById(int id) {

        try (Session session = sessionFactory.openSession()) {

            Role role = session.get(Role.class, id);

            if (role == null) {

                throw new EntityNotFoundException("Role", id);

            }
            return role;
        }
    }

    public List<Role> getAll(){
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Role ",Role.class).list();
        }
    }

    @Override
    public Role getByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Role  where role = :name", Role.class)
                    .setParameter("name", name)
                    .uniqueResult();
        }
    }
}
