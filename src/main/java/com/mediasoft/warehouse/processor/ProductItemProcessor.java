package com.mediasoft.warehouse.processor;

import com.mediasoft.warehouse.model.Product;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

/**
 * Процессор элементов для обработки продуктов.
 */
public class ProductItemProcessor implements ItemProcessor<Product, Product> {
    @Value("${app.scheduling.priceIncreasePercentage}")
    private String percent;

    /**
     * Обработка продукта, увеличение его цены на заданный процент.
     *
     * @param product продукт, который нужно обработать
     * @return обработанный продукт с обновленной ценой
     */
    @Override
    public Product process(Product product) {
        double currentPrice = product.getPrice();
        double newPrice = currentPrice * (1 + Double.parseDouble(percent) / 100);
        product.setPrice(newPrice);
        return product;
    }
}

