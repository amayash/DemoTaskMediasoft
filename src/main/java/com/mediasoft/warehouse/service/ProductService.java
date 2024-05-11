package com.mediasoft.warehouse.service;

import com.mediasoft.warehouse.dto.SaveProductDto;
import com.mediasoft.warehouse.error.exception.DuplicateArticleException;
import com.mediasoft.warehouse.error.exception.ProductNotFoundException;
import com.mediasoft.warehouse.filter.currency.CurrencyProvider;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.repository.ProductRepository;
import com.mediasoft.warehouse.search.AbstractProductFilter;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Сервис для управления товарами.
 */
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CurrencyProvider currencyProvider;
    private final ExchangeRateProvider exchangeRateProvider;

    /**
     * Получить все товары с пагинацией.
     *
     * @param page Номер страницы.
     * @param size Размер страницы.
     * @return Список всех товаров.
     */
    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(int page, int size) {
        BigDecimal exchangeRate = exchangeRateProvider.getExchangeRate(currencyProvider.getCurrency());
        Page<Product> products = productRepository.findAll(PageRequest.of(page - 1, size));
        products.forEach(product -> updateProductPrice(product, exchangeRate));
        return products;
    }

    /**
     * Получает все товары с учетом фильтров.
     *
     * @param pageable Pageable для работы с пагинацией и сортировкой результатов поиска
     * @param filters  список фильтров товаров
     * @return страница товаров, удовлетворяющих фильтрам
     */
    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable, List<AbstractProductFilter<?>> filters) {

        final Specification<Product> specification = (root, query, criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            for (AbstractProductFilter<?> filter : filters) {
                Specification<Product> operationSpec = switch (filter.getOperation()) {
                    case LIKE -> filter.likeOperation();
                    case GRATER_THAN_OR_EQ -> filter.greaterThanOrEqualsOperation();
                    case LESS_THAN_OR_EQ -> filter.lessThanOrEqualsOperation();
                    default -> filter.equalsOperation();
                };

                if (operationSpec != null) {
                    predicates.add(operationSpec.toPredicate(root, query, criteriaBuilder));
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return productRepository.findAll(specification, pageable);
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
        BigDecimal exchangeRate = exchangeRateProvider.getExchangeRate(currencyProvider.getCurrency());
        Page<Product> products =
                productRepository.findDistinctByNameContainingOrArticleContainingOrDescriptionContaining(search,
                        search, search, PageRequest.of(page - 1, size));
        products.forEach(product -> updateProductPrice(product, exchangeRate));
        return products;
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
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        updateProductPrice(product, exchangeRateProvider.getExchangeRate(currencyProvider.getCurrency()));
        return product;
    }

    /**
     * Обновляет цену товара в соответствии с указанным курсом валют.
     *
     * @param product      Товар, цена которого будет изменена по курсу.
     * @param exchangeRate Обменный курс, на который будет умножена текущая цена товара.
     */
    private void updateProductPrice(Product product, BigDecimal exchangeRate) {
        product.setPrice(product.getPrice().multiply(exchangeRate).setScale(2, RoundingMode.HALF_UP));
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
     * @throws ProductNotFoundException,  если товар не найден.
     * @throws DuplicateArticleException, если товар с указанным артикулом уже существует.
     */
    @Transactional
    public Product updateProduct(UUID productId, SaveProductDto updatedProductDto) {
        Product existingProduct = getProductById(productId);
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
        existingProduct.setIsAvailable(updatedProductDto.getIsAvailable());
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

    /**
     * Получить список товаров по идентификаторам
     *
     * @param productIds Идентификаторы товаров.
     * @return Товары с указанными идентификаторами.
     * @throws IllegalArgumentException, если не все товары найдены.
     */
    @Transactional(readOnly = true)
    public List<Product> getProductsByIds(Set<UUID> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new IllegalArgumentException("Not all products were found.");
        }
        return products;
    }
}