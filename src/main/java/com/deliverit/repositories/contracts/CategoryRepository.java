package com.deliverit.repositories.contracts;

import com.deliverit.models.Category;

import java.util.List;

public interface CategoryRepository {


    Category getById(int id);

    List<Category> getAllCategories();

}
