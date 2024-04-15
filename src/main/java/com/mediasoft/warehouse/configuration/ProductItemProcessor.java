package com.mediasoft.warehouse.configuration;

import com.mediasoft.warehouse.model.Product;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

public class ProductItemProcessor implements ItemProcessor<Product, Product> {
    @Value("${app.scheduling.priceIncreasePercentage}")
    private String percent;

    @Override
    public Product process(Product product) {
        double currentPrice = product.getPrice();
        double newPrice = currentPrice * (1 + Double.parseDouble(percent) / 100);
        product.setPrice(newPrice);
        return product;
    }
}

