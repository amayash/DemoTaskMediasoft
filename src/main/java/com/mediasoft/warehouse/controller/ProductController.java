package com.mediasoft.warehouse.controller;

import com.mediasoft.warehouse.dto.SaveProductDto;
import com.mediasoft.warehouse.dto.ViewProductDto;
import com.mediasoft.warehouse.filter.currency.CurrencyProvider;
import com.mediasoft.warehouse.model.Currency;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Rest-API контроллер для товаров.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final CurrencyProvider currencyProvider;

    /**
     * Получить список товаров с возможностью фильтрации и пагинацией.
     *
     * @param search Ключевое слово для фильтрации товаров (опционально).
     * @param page   Номер страницы.
     * @param size   Размер страницы.
     * @return Список DTO товаров, с фильтром или без.
     */
    @GetMapping
    public Page<ViewProductDto> get(@RequestParam(required = false) String search,
                                    @RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "5") int size) {
        Page<Product> productsPage;
        if (search == null) {
            productsPage = productService.getAllProducts(page, size);
        } else {
            productsPage = productService.getAllProducts(search, page, size);
        }
        Currency currency = currencyProvider.getCurrency();
        return productsPage.map(product -> {
            ViewProductDto viewProductDto = new ViewProductDto(product);
            viewProductDto.setCurrency(currency);
            return viewProductDto;
        });
    }

    /**
     * Получить товар по ID.
     *
     * @param id Идентификатор товара.
     * @return Информация о товаре.
     */
    @GetMapping("/{id}")
    public ViewProductDto getById(@PathVariable UUID id) {
        ViewProductDto productDto = new ViewProductDto(productService.getProductById(id));
        productDto.setCurrency(currencyProvider.getCurrency());
        return productDto;
    }

    /**
     * Создать товар.
     *
     * @param saveProductDto Информация о новом товаре.
     * @return Информация о созданном товаре.
     */
    @PostMapping
    public ViewProductDto create(@RequestBody @Valid SaveProductDto saveProductDto) {
        return new ViewProductDto(productService.createProduct(saveProductDto));
    }

    /**
     * Изменить информацию о товаре.
     *
     * @param id             Идентификатор товара.
     * @param saveProductDto Новая информация о товаре.
     * @return Информация об измененном товаре.
     */
    @PutMapping("/{id}")
    public ViewProductDto update(@PathVariable UUID id,
                                 @RequestBody @Valid SaveProductDto saveProductDto) {
        return new ViewProductDto(productService.updateProduct(id, saveProductDto));
    }

    /**
     * Удалить товар.
     *
     * @param id Идентификатор товара.
     * @return True, если товар успешно удален, в противном случае - false.
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable UUID id) {
        return productService.deleteProduct(id);
    }
}
