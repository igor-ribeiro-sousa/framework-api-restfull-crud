package com.crud.frame.service;

import java.util.List;
import java.util.Optional;

import com.crud.frame.repository.GenericRepository;

public abstract class GenericService<T, ID> {
	
    protected final GenericRepository<T, ID> repository;
    
    public GenericService(GenericRepository<T, ID> repository) {
        this.repository = repository;
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    public void deleteById(ID id) {
        repository.deleteById(id);
    }
}
