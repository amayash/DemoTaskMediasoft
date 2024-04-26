package com.mediasoft.warehouse.scheduling;

import com.mediasoft.warehouse.annotation.MeasureExecutionTime;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Запланированная задача для обновления цен товаров с использованием оптимизации по максимуму.
 */
@Component
@RequiredArgsConstructor
@Profile("!dev")
@ConditionalOnExpression(value = "#{'${app.scheduling.mode:none}'.equals('super')}")
@Slf4j
public class SuperOptimizedScheduler {
    private static final String QUERY = "UPDATE products SET price = price * (1 + ?/100) RETURNING *";
    @Value("#{new java.math.BigDecimal(\"${app.scheduling.priceIncreasePercentage:10}\")}")
    private BigDecimal priceIncreasePercentage;
    private final EntityManagerFactory entityManagerFactory;

    /**
     * Метод запускается периодически с фиксированной задержкой
     * для обновления цен товаров с использованием оптимизации по максимуму.
     */
    @Scheduled(fixedDelayString = "${app.scheduling.period}")
    @MeasureExecutionTime
    @Transactional
    public void increaseProductPrice() {
        log.info("Start SUPER Optimized Scheduler");

        final Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
        try (session) {
            session.doWork(connection -> {
                try (
                        final BufferedWriter fileWriter = new BufferedWriter(new FileWriter("products.txt"));
                        connection
                ) {
                    connection.setAutoCommit(false);
                    final PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
                    preparedStatement.setBigDecimal(1, priceIncreasePercentage);

                    final ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        fileWriter.write(buildString(resultSet));
                    }

                    connection.commit();
                } catch (Exception e) {
                    connection.rollback();
                    throw new RuntimeException(e);
                }
            });
        }

        log.info("End Super Optimized Scheduler");
    }

    /**
     * Строит строку из указанного ResultSet с разделением пробелом.
     *
     * @param resultSet ResultSet, содержащий данные для строки.
     * @return Строка с данными, где значения разделены пробелами.
     * @throws SQLException если произошла ошибка доступа к бд
     */
    private String buildString(final ResultSet resultSet) throws SQLException {
        return resultSet.getString("id") + " " +
                resultSet.getString("name") + " " +
                resultSet.getString("description") + " " +
                resultSet.getString("price") + " " +
                resultSet.getString("article");
    }
}