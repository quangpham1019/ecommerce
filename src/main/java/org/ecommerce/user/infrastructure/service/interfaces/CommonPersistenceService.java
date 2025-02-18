package org.ecommerce.user.infrastructure.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommonPersistenceService<T, ID> {

    List<T> findAll();
    Page<T> findAll(Pageable pageable);  // Pagination Support
    Optional<T> findById(ID id);

    boolean existsById(ID id);
    void deleteById(ID id);

    T updatePartial(ID id, T partialUpdate); // Partial Updates
    T save(T entity);
    List<T> saveAll(List<T> entities);

}
