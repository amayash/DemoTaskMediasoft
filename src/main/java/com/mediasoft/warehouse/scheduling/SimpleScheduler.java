package com.mediasoft.warehouse.scheduling;

import com.mediasoft.warehouse.annotation.MeasureExecutionTime;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Запланированная задача для обновления цен товаров.
 */
@Component
@RequiredArgsConstructor
@ConditionalOnExpression("'${app.scheduling.enabled}'.equals('true') and '${app.scheduling.optimization}'.equals('false')")
@Profile("!dev")
@Slf4j
public class SimpleScheduler {
    private final ProductRepository productRepository;
    @Value("${app.scheduling.priceIncreasePercentage}")
    private String percent;

    /**
     * Метод запускается периодически с фиксированной задержкой
     * для обновления цен товаров.
     */
    @Scheduled(fixedDelayString = "${app.scheduling.period}")
    @MeasureExecutionTime
    @Transactional
    public void scheduleFixedDelayTask() {
        final List<Product> productList = productRepository.findAll();
        log.info("Price 1: " + productList.get(0).getPrice());
        productList.forEach(product -> product.setPrice(product.getPrice() * (1 + Double.parseDouble(percent) / 100)));
        log.info("Price 1 + {}%: {}", percent, productList.get(0).getPrice());
        productRepository.saveAll(productList);
    }
}