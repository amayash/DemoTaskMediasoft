package com.mediasoft.warehouse.repository;

import com.mediasoft.warehouse.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для {@link Product}.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findDistinctByNameContainingOrArticleContainingOrDescriptionContaining(String name,
                                                                                         String article,
                                                                                         String description);
}
