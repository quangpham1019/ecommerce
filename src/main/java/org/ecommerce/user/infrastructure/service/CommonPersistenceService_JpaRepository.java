package org.ecommerce.user.infrastructure.service;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.ecommerce.user.infrastructure.service.interfaces.CommonPersistenceService;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.ecommerce.utils.ObjectUtils.convertToMap;

public class CommonPersistenceService_JpaRepository<T, ID> implements CommonPersistenceService<T, ID> {

    private final JpaRepository<T, ID> repository;

    public CommonPersistenceService_JpaRepository(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
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
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    @Override
    public List<T> saveAll(List<T> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional
    public T updatePartial(ID id, T user) {
        T existingEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Convert the user object into a map of non-null fields
        Map<String, Object> updates = convertToMap(user);

        // Apply updates dynamically
        updates.forEach((field, value) -> {
            BeanWrapper wrapper = new BeanWrapperImpl(existingEntity);
            if (wrapper.isWritableProperty(field)) {
                wrapper.setPropertyValue(field, value);
            }
        });

        return repository.save(existingEntity);
    }
}
