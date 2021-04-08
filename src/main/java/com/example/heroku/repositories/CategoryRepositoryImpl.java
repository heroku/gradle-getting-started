package com.deliverit.repositories;

import com.deliverit.exceptions.EntityNotFoundException;
import com.deliverit.models.Category;
import com.deliverit.models.City;
import com.deliverit.repositories.contracts.CategoryRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {


    private final SessionFactory sessionFactory;


    @Autowired
    public CategoryRepositoryImpl(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;

    }

    @Override
    public Category getById(int id) {

        try (Session session = sessionFactory.openSession()) {

            Category category = session.get(Category.class, id);

            if (category == null) {

                throw new EntityNotFoundException("Category", id);

            }
            return category;
        }
    }

    public List<Category> getAllCategories(){
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Category ",Category.class).list();
        }
    }
}
