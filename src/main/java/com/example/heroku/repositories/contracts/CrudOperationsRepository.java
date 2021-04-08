package com.deliverit.repositories.contracts;

public interface CrudOperationsRepository<T> {

    T create(T name);

    T update(T name);

    T delete(int id);
}
