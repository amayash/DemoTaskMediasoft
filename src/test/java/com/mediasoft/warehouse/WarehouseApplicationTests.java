package com.mediasoft.warehouse;

import com.exception.handler.error.exception.ProductNotFoundException;
import com.mediasoft.warehouse.dto.SaveProductDto;
import com.mediasoft.warehouse.error.exception.DuplicateArticleException;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.model.enums.ProductCategory;
import com.mediasoft.warehouse.repository.ProductRepository;
import com.mediasoft.warehouse.service.ProductService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit-тесты для бизнес-логики {@link ProductService}.
 */
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WarehouseApplicationTests {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    /**
     * Создает образец {@link SaveProductDto} для тестирования.
     *
     * @return Образец {@link SaveProductDto}.
     */
    private SaveProductDto createProductDto() {
        SaveProductDto product = new SaveProductDto();
        product.setName("name");
        product.setArticle("abc123");
        product.setDescription("description");
        product.setCategory(ProductCategory.BOOKS);
        product.setPrice(BigDecimal.valueOf(500));
        product.setQuantity(15L);
        product.setIsAvailable(true);
        return product;
    }

    /**
     * Очистка товаров перед всеми тестами.
     */
    @BeforeAll
    void init() {
        productRepository.deleteAll();
    }

    /**
     * Очистка товаров после каждого теста.
     */
    @AfterEach
    void down() {
        productRepository.deleteAll();
    }

    /**
     * Тест на создание товара.
     */
    @Test
    void testCreateProduct() {
        SaveProductDto product = createProductDto();
        Product createdProduct = productService.createProduct(product);

        assertProductEquals(product, createdProduct);
        assertEquals(createdProduct.getCreatedDate(),
                createdProduct.getLastQuantityChangeDate().toLocalDate());

        Assertions.assertThrows(DuplicateArticleException.class, () ->
                productService.createProduct(product));
    }

    /**
     * Тест на изменение товара.
     *
     * @throws InterruptedException если возникает ошибка при остановке потока.
     */
    @Test
    void testUpdateProduct() throws InterruptedException {
        SaveProductDto originalProduct = createProductDto();
        Product createdProduct = productService.createProduct(originalProduct);

        SaveProductDto updatedProductDto = createProductDto();
        updatedProductDto.setName("new name");
        updatedProductDto.setArticle("1234abc");
        updatedProductDto.setDescription("new description");
        updatedProductDto.setCategory(ProductCategory.ELECTRONICS);
        updatedProductDto.setPrice(BigDecimal.valueOf(5000));
        Thread.sleep(1000);
        Product updatedProduct = productService.updateProduct(createdProduct.getId(), updatedProductDto);

        assertProductEquals(updatedProductDto, updatedProduct);
        assertEquals(
                createdProduct.getLastQuantityChangeDate().truncatedTo(ChronoUnit.SECONDS),
                updatedProduct.getLastQuantityChangeDate().truncatedTo(ChronoUnit.SECONDS)
        );

        updatedProductDto.setQuantity(150L);
        Thread.sleep(1000);
        Product updatedProductWithQuantityChange = productService.updateProduct(createdProduct.getId(), updatedProductDto);
        Assertions.assertNotEquals(
                createdProduct.getLastQuantityChangeDate().truncatedTo(ChronoUnit.SECONDS),
                updatedProductWithQuantityChange.getLastQuantityChangeDate().truncatedTo(ChronoUnit.SECONDS)
        );

        originalProduct.setArticle("another article");
        Product productWithAnotherArticle = productService.createProduct(originalProduct);
        updatedProductDto.setArticle(productWithAnotherArticle.getArticle());
        Assertions.assertThrows(DuplicateArticleException.class, () ->
                productService.updateProduct(createdProduct.getId(), updatedProductDto));
    }

    /**
     * Тест на получение товара.
     */
    @Test
    void testGetProduct() {
        Product createdProduct = productService.createProduct(createProductDto());
        Product product = productService.getProductById(createdProduct.getId());
        assertProductEquals(new SaveProductDto(createdProduct), product);
        assertEquals(createdProduct.getCreatedDate(), product.getCreatedDate());
        assertEquals(
                createdProduct.getLastQuantityChangeDate().truncatedTo(ChronoUnit.SECONDS),
                product.getLastQuantityChangeDate().truncatedTo(ChronoUnit.SECONDS)
        );
    }

    /**
     * Тест на получение товара, которого не существует.
     */
    @Test
    void testInvalidGetProduct() {
        Assertions.assertThrows(ProductNotFoundException.class, () ->
                productService.getProductById(UUID.randomUUID()));
    }

    /**
     * Тест на получение списка товаров.
     */
    @Test
    void testGetProducts() {
        SaveProductDto product = createProductDto();
        productService.createProduct(product);

        product.setArticle("new_article");
        productService.createProduct(product);

        Page<Product> products = productService.getAllProducts(1, 3);
        assertEquals(2, products.getTotalElements());
    }

    /**
     * Тест на получение списка товаров с фильтрами.
     */
    @Test
    void testGetFilteredProducts() {
        SaveProductDto product = createProductDto();
        product.setDescription("name");
        productService.createProduct(product);
        product.setName("difference");
        product.setDescription("difference");
        product.setArticle("diff_article1");
        productService.createProduct(product);

        product.setArticle("name");
        productService.createProduct(product);

        product.setArticle("diff_article2");
        product.setDescription("name");
        productService.createProduct(product);

        Page<Product> products = productService.getAllProducts(1, 5);
        assertEquals(4, products.getTotalElements());
        products = productService.getAllProducts("name", 1, 5);
        assertEquals(3, products.getTotalElements());
    }

    /**
     * Тест на удаление товара.
     */
    @Test
    void testDeleteProduct() {
        SaveProductDto product = createProductDto();
        Product createdProduct = productService.createProduct(product);
        Assertions.assertTrue(productService.deleteProduct(createdProduct.getId()));
        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.getProductById(createdProduct.getId()));
    }

    /**
     * Проверка, что переданный товар соответствует ожидаемым значениям по полям:
     * название, артикул, описание, категория, цена, количество.
     *
     * @param expected Ожидаемый товар.
     * @param actual   Фактический товар.
     */
    private void assertProductEquals(SaveProductDto expected, Product actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getArticle(), actual.getArticle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getCategory(), actual.getCategory());
        assertEquals(0, expected.getPrice().compareTo(actual.getPrice()));
        assertEquals(expected.getQuantity(), actual.getQuantity());
    }
}