package com.mediasoft.warehouse.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediasoft.warehouse.dto.SaveProductDto;
import com.mediasoft.warehouse.dto.ViewCurrenciesDto;
import com.mediasoft.warehouse.error.exception.DuplicateArticleException;
import com.mediasoft.warehouse.error.exception.ProductNotFoundException;
import com.mediasoft.warehouse.filter.currency.CurrencyProvider;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сервис для управления товарами.
 */
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CurrencyServiceClient currencyServiceClient;
    private final CurrencyProvider currencyProvider;

    /**
     * Получить все товары с пагинацией.
     *
     * @param page Номер страницы.
     * @param size Размер страницы.
     * @return Список всех товаров.
     */
    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(int page, int size) {
        BigDecimal exchangeRate = getExchangeRate(currencyProvider.getCurrency(), getCurrencies());
        Page<Product> products = productRepository.findAll(PageRequest.of(page - 1, size));
        products.forEach(product -> updateProductPrice(product, exchangeRate));
        return products;
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
        BigDecimal exchangeRate = getExchangeRate(currencyProvider.getCurrency(), getCurrencies());
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
        updateProductPrice(product, getExchangeRate(currencyProvider.getCurrency(), getCurrencies()));
        return product;
    }

    /**
     * Получает данные о курсах валют.
     *
     * @return Объект {@link ViewCurrenciesDto}, содержащий данные о курсах валют.
     */
    private ViewCurrenciesDto getCurrencies() {
        ViewCurrenciesDto currencyDto;
        try {
            currencyDto = currencyServiceClient.getCurrencies();
        } catch (Exception e) {
            currencyDto = readCurrenciesFromJson();
        }
        return currencyDto;
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
     * Возвращает обменный курс для указанной валюты.
     *
     * @param currency    Код валюты (например, "USD").
     * @param currencyDto Объект {@link ViewCurrenciesDto} с данными о курсах валют.
     * @return Обменный курс для указанной валюты.
     */
    private BigDecimal getExchangeRate(String currency, ViewCurrenciesDto currencyDto) {
        return switch (currency) {
            case "CNY" -> currencyDto.getCNY();
            case "USD" -> currencyDto.getUSD();
            case "EUR" -> currencyDto.getEUR();
            default -> BigDecimal.ONE;
        };
    }

    /**
     * Считывает данные о курсах валют из JSON-файла в ресурсах.
     *
     * @return Объект {@link ViewCurrenciesDto}, содержащий данные о курсах валют из JSON-файла.
     * @throws RuntimeException если не удалось прочитать данные из файла.
     */
    private ViewCurrenciesDto readCurrenciesFromJson() {
        try {
            ClassPathResource resource = new ClassPathResource("exchange-rate.json");
            byte[] jsonData = FileCopyUtils.copyToByteArray(resource.getInputStream());
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonData, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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