package org.ecommerce.user.infrastructure.service;
import org.ecommerce.user.infrastructure.service.interfaces.CommonPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public class CommonPersistenceServiceImpl<T, ID> implements CommonPersistenceService<T, ID> {

    private final JpaRepository<T, ID> repository;

    public CommonPersistenceServiceImpl(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public Iterable<T> saveAll(Iterable<T> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public List<T> findAllByIds(Iterable<ID> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    @Override
    public void delete(T entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public long countByAttribute(String attribute) {
        // Custom logic to count based on a condition or attribute
        return 0;  // Placeholder for actual implementation
    }

    @Override
    public void deleteByCondition(String condition) {
        // Custom delete logic based on condition
    }

    @Override
    public List<T> findByCondition(String condition) {
        // Custom find logic based on condition
        return null;  // Placeholder for actual implementation
    }

    @Override
    public T update(T entity) {
        // You may want to update the entity depending on your needs.
        // For now, JPA handles updates via save().
        return repository.save(entity);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public void executeTransaction(Runnable transactionLogic) {
        // Implement a custom transactional logic if needed
    }
}
