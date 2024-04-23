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

import javax.sql.DataSource;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

/**
 * Запланированная задача для обновления цен товаров с использованием PreparedStatements.
 */
@Component
@RequiredArgsConstructor
@ConditionalOnExpression(value = "#{'${app.scheduling.mode:none}'.equals('optimization') and " +
        "!${app.scheduling.optimization.spring-batch:false}}")
@Profile("!dev")
@Slf4j
public class OptimizedSchedulerWithPreparedStatements implements PriceScheduler {
    private final DataSource dataSource;
    @Value("#{new java.math.BigDecimal(\"${app.scheduling.priceIncreasePercentage:10}\")}")
    private BigDecimal percent;
    private static final int BATCH_SIZE = 100000;
    private final EntityManagerFactory entityManagerFactory;

    /**
     * Метод запускается периодически с фиксированной задержкой
     * для обновления цен товаров с использованием PreparedStatements.
     */
    @Scheduled(fixedDelayString = "${app.scheduling.period}")
    @MeasureExecutionTime
    @Transactional
    public void scheduleFixedDelayTask() {
        log.info("Start.");
        final Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
        try (session) {
            session.doWork(connection -> {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("products.txt"));
                     connection
                ) {
                    connection.setAutoCommit(false);
                    String selectQuery = "SELECT * FROM products FOR UPDATE";
                    String updateQuery = "UPDATE products SET price = ? WHERE id = ?";

                    Statement selectStatement = connection.createStatement();
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

                    ResultSet resultSet = selectStatement.executeQuery(selectQuery);
                    int columnCount = resultSet.getMetaData().getColumnCount();
                    int count = 0;
                    while (resultSet.next()) {
                        UUID id = (UUID) resultSet.getObject("id");

                        String row = buildRowString(resultSet, columnCount);
                        writer.write(row);
                        writer.newLine();

                        updateStatement.setBigDecimal(1,
                                getNewPrice(resultSet.getBigDecimal("price"), percent));
                        updateStatement.setObject(2, id);
                        updateStatement.addBatch();

                        count++;
                        if (count % BATCH_SIZE == 0) {
                            updateStatement.executeBatch();
                        }
                    }
                    updateStatement.executeBatch();
                    connection.commit();
                } catch (Exception exception) {
                    try {
                        connection.rollback();
                    } catch (SQLException sqlException) {
                        log.error("Failed to rollback transaction", sqlException);
                    }
                    log.error("The following exception was received", exception);
                }
            });
        }
    }

    /**
     * Строит строку из указанного ResultSet с разделением запятыми.
     *
     * @param resultSet   ResultSet, содержащий данные для строки.
     * @param columnCount Количество столбцов в ResultSet.
     * @return Строка с данными, где значения разделены запятыми.
     * @throws SQLException если произошла ошибка доступа к бд
     */
    private String buildRowString(ResultSet resultSet, int columnCount) throws SQLException {
        StringBuilder row = new StringBuilder();
        for (int i = 1; i <= columnCount; i++) {
            if (i == columnCount) {
                row.append(resultSet.getString(i));
            } else {
                row.append(resultSet.getString(i)).append(", ");
            }
        }
        return row.toString();
    }
}