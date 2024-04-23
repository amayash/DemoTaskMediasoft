package com.mediasoft.warehouse;

import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.model.enums.FieldName;
import com.mediasoft.warehouse.model.enums.ProductCategory;
import com.mediasoft.warehouse.repository.ProductRepository;
import com.mediasoft.warehouse.service.ProductService;
import com.mediasoft.warehouse.service.operation.AbstractProductFilter;
import com.mediasoft.warehouse.service.operation.NumberProductFilter;
import com.mediasoft.warehouse.service.operation.StringProductFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тестирование фильтров ProductService.
 */
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductServiceFilterTest {
    @Autowired
    ProductRepository productRepository;
    ProductService productService;

    /**
     * Создает объект {@link Product}.
     *
     * @param num     номер товара
     * @param price   цена товара
     * @param count   количество товара
     * @param strings дополнительные строки
     * @return созданный товар
     */
    private Product createProduct(int num, int price, long count, String... strings) {
        Product product = new Product();
        if (strings.length > 0) {
            product.setName(strings[0]);
        } else {
            product.setName("Product " + num);
        }
        product.setArticle("Article " + num);
        product.setDescription("Description" + num);
        product.setCategory(ProductCategory.BOOKS);
        product.setPrice(BigDecimal.valueOf(price));
        product.setQuantity(count);
        return product;
    }

    /**
     * Добавление товаров перед всеми тестами и инициализация {@link ProductService}.
     */
    @BeforeAll
    void init() {
        productService = new ProductService(productRepository);
        productRepository.save(createProduct(1, 500, 50));
        productRepository.save(createProduct(2, 600, 60));
        productRepository.save(createProduct(3, 650, 70, "Product"));
        productRepository.save(createProduct(4, 670, 80, "4 Product"));
        productRepository.save(createProduct(5, 700, 90));
    }

    /**
     * Проверяет фильтры.
     */
    @Test
    void testFilters() {
        BigDecimal minPrice = BigDecimal.valueOf(550);
        BigDecimal maxPrice = BigDecimal.valueOf(650);
        BigDecimal similarPrice = BigDecimal.valueOf(620);

        NumberProductFilter priceFilter1 = new NumberProductFilter();
        priceFilter1.setField(FieldName.PRICE);
        priceFilter1.setOperation(">=");
        priceFilter1.setSearchParam(minPrice);

        NumberProductFilter priceFilter2 = new NumberProductFilter();
        priceFilter2.setField(FieldName.PRICE);
        priceFilter2.setOperation("<=");
        priceFilter2.setSearchParam(maxPrice);

        NumberProductFilter priceFilter3 = new NumberProductFilter();
        priceFilter3.setField(FieldName.PRICE);
        priceFilter3.setOperation("~");
        priceFilter3.setSearchParam(similarPrice);

        StringProductFilter stringFilter1 = new StringProductFilter();
        stringFilter1.setField(FieldName.NAME);
        stringFilter1.setOperation("<=");
        stringFilter1.setSearchParam("Product");

        StringProductFilter stringFilter2 = new StringProductFilter();
        stringFilter2.setField(FieldName.LAST_QUANTITY_CHANGE_DATE);
        stringFilter2.setOperation("~");
        stringFilter2.setSearchParam(LocalDateTime.now().minusDays(2).toString());

        StringProductFilter stringFilter3 = new StringProductFilter();
        stringFilter3.setField(FieldName.CREATED_DATE);
        stringFilter3.setOperation("~");
        stringFilter3.setSearchParam(LocalDate.now().minusDays(2).toString());

        List<AbstractProductFilter<?>> filters = new ArrayList<>();
        filters.add(priceFilter1);
        filters.add(priceFilter2);
        filters.add(priceFilter3);
        filters.add(stringFilter1);
        filters.add(stringFilter2);
        filters.add(stringFilter3);

        Page<Product> productsPage = productService.getAllProducts(PageRequest.of(0, 5), filters);
        assertEquals(1, productsPage.getContent().size(), "Ожидается 1 товар в результате фильтрации");
        Product product = productsPage.getContent().get(0);

        assertTrue(product.getName().startsWith("Product"), "Ожидается товар с названием, начинающимся на 'Product'");
        assertTrue(product.getPrice().compareTo(minPrice) >= 0 && product.getPrice().compareTo(maxPrice) <= 0,
                String.format("Цена товара должна быть между %s и %s", minPrice, maxPrice));

        BigDecimal tenPercentOfSimilarPrice = similarPrice.multiply(BigDecimal.valueOf(0.1));
        BigDecimal priceDifference = product.getPrice().subtract(similarPrice).abs();
        assertTrue(priceDifference.compareTo(tenPercentOfSimilarPrice) <= 0,
                String.format("Цена товара должна отличаться от %s не более чем на 10%%", similarPrice));
    }
}
