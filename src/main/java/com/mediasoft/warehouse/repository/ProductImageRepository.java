package com.mediasoft.warehouse.repository;

import com.mediasoft.warehouse.model.ProductImage;
import com.mediasoft.warehouse.model.ProductImageKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для {@link ProductImageKey}.
 */
public interface ProductImageRepository extends JpaRepository<ProductImage, ProductImageKey> {
    List<ProductImage> findById_ProductId(UUID productId);
}
