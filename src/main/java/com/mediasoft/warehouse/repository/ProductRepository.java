package com.mediasoft.warehouse.repository;

import com.mediasoft.warehouse.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для {@link Product}.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    /**
     * Получить страницу товаров с фильтрами по имени, артикулу или описанию.
     *
     * @param name        Строка для поиска в названии товара.
     * @param article     Строка для поиска в артикуле товара.
     * @param description Строка для поиска в описании товара.
     * @param pageable    Информация о пагинации.
     * @return Страница товаров, удовлетворяющих критериям поиска.
     */
    Page<Product> findDistinctByNameContainingOrArticleContainingOrDescriptionContaining(String name,
                                                                                         String article,
                                                                                         String description,
                                                                                         Pageable pageable);

    /**
     * Получить информацию о том, существует ли товар с указанным артикулом.
     *
     * @param article Артикул для проверки.
     * @return Флаг, указывающий существует ли товар с указанным артикулом.
     */
    boolean existsByArticle(String article);
}
