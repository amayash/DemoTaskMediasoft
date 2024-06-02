package com.mediasoft.warehouse.controller;

import com.mediasoft.warehouse.dto.SaveProductDto;
import com.mediasoft.warehouse.dto.ViewProductDto;
import com.mediasoft.warehouse.filter.currency.CurrencyProvider;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.model.enums.Currency;
import com.mediasoft.warehouse.search.AbstractProductFilter;
import com.mediasoft.warehouse.service.FileService;
import com.mediasoft.warehouse.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
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
    private final CurrencyProvider currencyProvider;
    private final FileService fileService;

    /**
     * Загружает файл, связанный с товаром.
     *
     * @param productId идентификатор товара
     * @param file      файл для загрузки
     * @return строка с подтверждением успешной загрузки и S3 ключом
     * @throws IOException если возникает ошибка ввода-вывода
     */
    @PostMapping("/{productId}/upload")
    public String uploadFile(@PathVariable UUID productId,
                             @RequestParam("file") MultipartFile file) throws IOException {
        String key = fileService.uploadFile(file, productId);

        return "File uploaded successfully. Key: " + key;
    }

    /**
     * Скачивает изображения, связанные с товаром, в виде ZIP-архива.
     *
     * @param productId идентификатор товара
     * @param response  ответ для передачи ZIP-архива
     * @throws IOException если возникает ошибка ввода-вывода
     */
    @GetMapping("/{productId}/download")
    public void downloadImages(@PathVariable UUID productId, HttpServletResponse response) throws IOException {
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"images.zip\"");

        try (OutputStream outputStream = response.getOutputStream()) {
            fileService.downloadFiles(productId, outputStream);
        }
    }

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
     * Поиск товаров с использованием фильтров.
     *
     * @param pageable               Pageable для работы с пагинацией и сортировкой результатов поиска
     * @param abstractProductFilters список фильтров товаров
     * @return страницу товаров, удовлетворяющих фильтрам, преобразованную в список DTO товаров
     */
    @PostMapping("/search")
    public Page<ViewProductDto> searchProducts(Pageable pageable,
                                               @RequestBody @Valid List<AbstractProductFilter<?>> abstractProductFilters) {
        return productService.getAllProducts(pageable, abstractProductFilters).map(ViewProductDto::new);
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
