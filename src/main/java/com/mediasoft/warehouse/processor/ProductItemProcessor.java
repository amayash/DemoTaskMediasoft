package com.mediasoft.warehouse.processor;

import com.mediasoft.warehouse.model.Product;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Процессор элементов для обработки продуктов.
 */
public class ProductItemProcessor implements ItemProcessor<Product, Product> {
    @Value("#{new java.math.BigDecimal(\"${app.scheduling.priceIncreasePercentage:10}\")}")
    private BigDecimal percent;

    /**
     * Обработка продукта, увеличение его цены на заданный процент.
     *
     * @param product продукт, который нужно обработать
     * @return обработанный продукт с обновленной ценой
     */
    @Override
    public Product process(Product product) {
        BigDecimal currentPrice = product.getPrice();
        BigDecimal percentage = percent.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal increase = currentPrice.multiply(percentage);
        BigDecimal newPrice = currentPrice.add(increase);
        product.setPrice(newPrice);
        return product;
    }
}

