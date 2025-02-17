package org.ecommerce.user.infrastructure.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommonPersistenceService<T, ID> {

    // Save a single entity
    T save(T entity);

    // Save multiple entities
    Iterable<T> saveAll(Iterable<T> entities);

    // Find an entity by its ID
    Optional<T> findById(ID id);

    // Find all entities
    List<T> findAll();

    // Find multiple entities by their IDs
    List<T> findAllByIds(Iterable<ID> ids);

    // Delete an entity by its ID
    void deleteById(ID id);

    // Delete a specific entity
    void delete(T entity);

    // Delete all entities
    void deleteAll();

    // Check if an entity exists by its ID
    boolean existsById(ID id);

    // Count all entities
    long count();

    // Count entities by an attribute or condition
    long countByAttribute(String attribute);

    // Delete entities by condition (custom logic)
    void deleteByCondition(String condition);

    // Find entities by condition (custom query)
    List<T> findByCondition(String condition);

    // Update an entity (if applicable in your context)
    T update(T entity);

    // Find entities with pagination
    Page<T> findAll(Pageable pageable);

    // Transactional execution for complex operations
    void executeTransaction(Runnable transactionLogic);
}
