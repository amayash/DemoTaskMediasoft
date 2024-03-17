package com.mediasoft.warehouse.service;

import com.mediasoft.warehouse.dto.SaveProductDto;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.repository.ProductRepository;
import com.mediasoft.warehouse.error.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
     * Получить все товары.
     *
     * @return Список всех товаров.
     */
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Получить товары с учетом параметра поиска.
     *
     * @param search Строка для поиска в названии, артикуле и описании товара.
     * @return Список товаров, удовлетворяющих критериям поиска.
     */
    @Transactional(readOnly = true)
    public List<Product> getAllProducts(String search) {
        return productRepository.findDistinctByNameContainingOrArticleContainingOrDescriptionContaining(search, search, search);
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
     */
    @Transactional
    public Product createProduct(SaveProductDto saveProductDto) {
        return productRepository.save(new Product(saveProductDto));
    }

    /**
     * Изменить информацию о товаре.
     *
     * @param productId         Идентификатор товара, который нужно изменить.
     * @param updatedProductDto DTO с измененной информацией о товаре.
     * @return Измененный товар.
     * @throws ProductNotFoundException, если товар не найден.
     */
    @Transactional
    public Product updateProduct(UUID productId, SaveProductDto updatedProductDto) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        existingProduct.setName(updatedProductDto.getName());
        existingProduct.setArticle(updatedProductDto.getArticle());
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