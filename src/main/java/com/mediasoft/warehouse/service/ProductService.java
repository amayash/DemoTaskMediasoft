package com.mediasoft.warehouse.service;

import com.mediasoft.warehouse.dto.ProductFilterDto;
import com.mediasoft.warehouse.dto.SaveProductDto;
import com.mediasoft.warehouse.dto.ViewProductDto;
import com.mediasoft.warehouse.error.exception.ProductNotFoundException;
import com.mediasoft.warehouse.error.exception.DuplicateArticleException;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.repository.ProductRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Сервис для управления товарами.
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Получить все товары с пагинацией.
     *
     * @param page Номер страницы.
     * @param size Размер страницы.
     * @return Список всех товаров.
     */
    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(int page, int size) {
        return productRepository.findAll(PageRequest.of(page - 1, size));
    }

    /**
     * Получить все товары с пагинацией.
     *
     * @return Список всех товаров.
     */
    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(final ProductFilterDto filter) {
//        CriteriaBuilder builder = session.getCriteriaBuilder();
//        CriteriaQuery<Product> criteriaQuery = builder.createQuery(Product.class);
//        Root<Product> products = criteriaQuery.from(Product.class);
//
//        criteriaQuery.select(criteriaQuery.from(Product.class))
//                .where(builder.like(products.get("name"), "%" + name + "%"));
//        return productRepository.findAll(PageRequest.of(page - 1, size));
        final PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getSize());
        final Specification<Product> specification = (root, query, criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if (filter.getName() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + filter.getName() + "%"));
            }
            if (filter.getQuantity() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("quantity"), filter.getQuantity()));
            }
            if (filter.getPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), filter.getPrice()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return productRepository.findAll(specification, pageRequest);
    }


    /**
     * Получить товары с учетом параметра поиска.
     *
     * @param search Строка для поиска в названии, артикуле и описании товара.
     * @param page   Номер страницы.
     * @param size   Размер страницы.
     * @return Список товаров, удовлетворяющих критериям поиска.
     */
    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(String search, int page, int size) {
        return productRepository.findDistinctByNameContainingOrArticleContainingOrDescriptionContaining(search,
                search, search, PageRequest.of(page - 1, size));
    }

    /**
     * Получить товар по идентификатору.
     *
     * @param productId Идентификатор товара.
     * @return Товар с указанным идентификатором.
     * @throws ProductNotFoundException, если товар не найден.
     */
    @Transactional(readOnly = true)
    public Product getProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    /**
     * Создать товар.
     *
     * @param saveProductDto DTO с информацией о новом товаре.
     * @return Созданный товар.
     * @throws DuplicateArticleException, если товар с указанным артикулом уже существует.
     */
    @Transactional
    public Product createProduct(SaveProductDto saveProductDto) {
        String article = saveProductDto.getArticle();
        if (productRepository.existsByArticle(article)) {
            throw new DuplicateArticleException(article);
        }
        return productRepository.save(new Product(saveProductDto));
    }

    /**
     * Изменить информацию о товаре.
     *
     * @param productId         Идентификатор товара, который нужно изменить.
     * @param updatedProductDto DTO с измененной информацией о товаре.
     * @return Измененный товар.
     * @throws ProductNotFoundException, если товар не найден.
     * @throws DuplicateArticleException, если товар с указанным артикулом уже существует.
     */
    @Transactional
    public Product updateProduct(UUID productId, SaveProductDto updatedProductDto) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        String article = updatedProductDto.getArticle();
        if (productRepository.existsByArticle(article)
                && !existingProduct.getArticle().equals(article)) {
            throw new DuplicateArticleException(article);
        }

        existingProduct.setName(updatedProductDto.getName());
        existingProduct.setArticle(article);
        existingProduct.setDescription(updatedProductDto.getDescription());
        existingProduct.setCategory(updatedProductDto.getCategory());
        existingProduct.setPrice(updatedProductDto.getPrice());
        if (!existingProduct.getQuantity().equals(updatedProductDto.getQuantity())) {
            existingProduct.setQuantity(updatedProductDto.getQuantity());
            existingProduct.setLastQuantityChangeDate(LocalDateTime.now());
        }

        return productRepository.save(existingProduct);
    }

    /**
     * Удалить товар по его идентификатору.
     *
     * @param productId Идентификатор товара, который нужно удалить.
     * @return True, если товар успешно удален, в противном случае - false.
     */
    @Transactional
    public boolean deleteProduct(UUID productId) {
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            return true;
        }
        return false;
    }
}