package com.mediasoft.warehouse;

import com.mediasoft.warehouse.dto.ProductFilterDto;
import com.mediasoft.warehouse.dto.SaveProductDto;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.model.enums.ProductCategory;
import com.mediasoft.warehouse.repository.ProductRepository;
import com.mediasoft.warehouse.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest(showSql = true)
public class JpaUnitTestProductFilters {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    /**
     * Создает образец {@link SaveProductDto} для тестирования.
     *
     * @return Образец {@link SaveProductDto}.
     */
    private Product createProduct(int num, int price, long count) {
        Product product = new Product();
        product.setName("Product " + num);
        product.setArticle("Article " + num);
        product.setDescription("Description" + num);
        product.setCategory(ProductCategory.BOOKS);
        product.setPrice(BigDecimal.valueOf(price));
        product.setQuantity(count);
        return product;
    }

    /**
     * Добавление товаров перед всеми тестами.
     */
    @BeforeEach
    void init() {
        productRepository.save(createProduct(1, 500, 50L));
        productRepository.save(createProduct(2, 600, 60L));
        productRepository.save(createProduct(3, 700, 70L));
    }

    /**
     * Тест на создание товара.
     */
    @Test
    void testFilters() {
        List<ProductFilterDto<?>> stringFilters = new ArrayList<>();
        productService.getAllProducts(1, 5, stringFilters);
    }
}
