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
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@ConditionalOnExpression("'${spring.scheduling.enabled}'.equals('true') and '${spring.scheduling.optimization}'.equals('false')")
@Profile("prod")
@Slf4j
public class SimpleScheduler {
    private final ProductRepository productRepository;

    @Value("${spring.scheduling.priceIncreasePercentage}")
    private String percent;

    @Scheduled(fixedDelayString = "${spring.scheduling.period}")
    @MeasureExecutionTime
    @Transactional
    public void scheduleFixedDelayTask() {
        List<Product> productList = productRepository.findAll();
        log.info("Price 1: " + productList.get(0).getPrice());
        productList = productList.stream()
                .peek(product -> product.setPrice(product.getPrice() * (1 + Double.parseDouble(percent) / 100)))
                .collect(Collectors.toList());
        log.info("Price 1 + {}%: {}", percent, productList.get(0).getPrice());
        productRepository.saveAll(productList);
    }
}