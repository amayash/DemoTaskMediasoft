package com.mediasoft.warehouse.scheduling;

import com.mediasoft.warehouse.annotation.MeasureExecutionTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Запланированная задача для обновления цен товаров с использованием PreparedStatements.
 */
@Component
@RequiredArgsConstructor
@ConditionalOnExpression("'${app.scheduling.enabled}'.equals('true') and '${app.scheduling.optimization}'.equals('true')" +
        " and '${app.scheduling.spring-batching}'.equals('false')")
@Profile("!dev")
@Slf4j
public class OptimizedSchedulerWithPreparedStatements {
    private final DataSource dataSource;
    @Value("${app.scheduling.priceIncreasePercentage}")
    private String percent;
    private static final int BATCH_SIZE = 100000;

    /**
     * Метод запускается периодически с фиксированной задержкой
     * для обновления цен товаров с использованием PreparedStatements.
     */
    @Scheduled(fixedDelayString = "${app.scheduling.period}")
    @MeasureExecutionTime
    @Transactional
    public void scheduleFixedDelayTask() {
        log.info("Start.");
        try (Connection connection = dataSource.getConnection();
             BufferedWriter writer = new BufferedWriter(new FileWriter("products.txt"))) {
            connection.setAutoCommit(false);
            String selectQuery = "SELECT * FROM products";
            String updateQuery = "UPDATE products SET price = ? WHERE id = ?";

            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                 PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

                ResultSet resultSet = selectStatement.executeQuery();
                int columnCount = resultSet.getMetaData().getColumnCount();
                int count = 0;
                while (resultSet.next()) {
                    UUID id = (UUID) resultSet.getObject("id");
                    double currentPrice = resultSet.getDouble("price");
                    double newPrice = currentPrice * (1 + Double.parseDouble(percent) / 100);

                    StringBuilder row = new StringBuilder();
                    for (int i = 1; i <= columnCount; i++) {
                        if (i == columnCount) {
                            row.append(resultSet.getString(i));
                        } else {
                            row.append(resultSet.getString(i)).append(", ");
                        }
                    }
                    writer.write(row.toString());
                    writer.newLine();

                    updateStatement.setDouble(1, newPrice);
                    updateStatement.setObject(2, id);
                    updateStatement.addBatch();

                    count++;
                    if (count % BATCH_SIZE == 0) {
                        updateStatement.executeBatch();
                    }
                }
                updateStatement.executeBatch();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                log.error("Failed to execute SQL query", e);
            } finally {
                writer.close();
                connection.setAutoCommit(true);
            }
        } catch (SQLException | IOException e) {
            log.error("Failed to establish database connection or write to file", e);
        }
    }
}