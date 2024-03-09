package com.mediasoft.warehouse.controller;

import com.mediasoft.warehouse.dto.SaveProductDto;
import com.mediasoft.warehouse.dto.ViewProductDto;
import com.mediasoft.warehouse.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Rest-API контроллер для товаров.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    /**
     * Получить список товаров с возможностью фильтрации.
     *
     * @param search Ключевое слово для фильтрации товаров (опционально).
     * @return Список DTO товаров, с фильтром или без.
     */
    @GetMapping
    public List<ViewProductDto> get(@RequestParam(value = "search", required = false) String search) {
        if (search == null)
            return productService.getAllProducts()
                    .stream()
                    .map(ViewProductDto::new)
                    .toList();
        else
            return productService.getAllProducts(search)
                    .stream()
                    .map(ViewProductDto::new)
                    .toList();
    }

    /**
     * Получить товар по ID.
     *
     * @param id Идентификатор товара.
     * @return Информация о товаре.
     */
    @GetMapping("/{id}")
    public ViewProductDto getById(@PathVariable UUID id) {
        return new ViewProductDto(productService.getProductById(id));
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
