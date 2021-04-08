package com.deliverit.repositories.contracts;

import java.util.List;
import java.util.Optional;

public interface SimpleOperationRepository<T> {
    List<T> getAll();

    T getById(int id);
}
