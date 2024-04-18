package com.mediasoft.warehouse.configuration.batch;

import com.mediasoft.warehouse.model.Product;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурационный класс для настройки и определения задач пакетной обработки.
 */
@Configuration
@ConditionalOnExpression("'${app.scheduling.enabled}'.equals('true') and '${app.scheduling.optimization}'.equals('true')" +
        " and '${app.scheduling.spring-batching}'.equals('true')")
@Profile("!dev")
public class BatchConfiguration {
    /**
     * Создает читатель для чтения данных из базы данных.
     *
     * @param dataSource источник данных для чтения
     * @return JdbcPagingItemReader для чтения данных из базы данных
     */
    @Bean
    public JdbcPagingItemReader<Product> reader(DataSource dataSource) {
        JdbcPagingItemReader<Product> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        // Размер пакета - количество записей, выбираемых за один раз из базы данных
        reader.setFetchSize(10000);
        // Размер страницы - количество записей, выбираемых из базы данных за один запрос пагинации
        reader.setPageSize(1000);
        reader.setRowMapper(productRowMapper());
        reader.setQueryProvider(queryProvider());
        return reader;
    }

    /**
     * Создает маппер строк для маппинга результатов запроса.
     *
     * @return маппер строк для объектов Product
     */
    @Bean
    public RowMapper<Product> productRowMapper() {
        return new BeanPropertyRowMapper<>(Product.class);
    }

    /**
     * Создает поставщик запросов для использования считывателем.
     *
     * @return поставщик запросов с пагинируемыми данными
     */
    @Bean
    public PostgresPagingQueryProvider queryProvider() {
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.ASCENDING);
        PostgresPagingQueryProvider queryProvider = new PostgresPagingQueryProvider();
        queryProvider.setSelectClause("SELECT *");
        queryProvider.setFromClause("FROM products");
        queryProvider.setSortKeys(sortKeys);

        return queryProvider;
    }

    /**
     * Создает экземпляр ItemProcessor для обработки элементов.
     *
     * @return экземпляр ItemProcessor
     */
    @Bean
    public ItemProcessor<Product, Product> processor() {
        return new ProductItemProcessor();
    }

    /**
     * Создает писатель для записи данных в базу данных.
     *
     * @param dataSource источник данных для записи
     * @return JdbcBatchItemWriter для записи данных в базу данных
     */
    @Bean
    public JdbcBatchItemWriter<Product> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Product>()
                .sql("UPDATE products SET price = :price WHERE id = :id")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    /**
     * Создает писатель для записи данных в файл.
     *
     * @return FlatFileItemWriter для записи данных в файл
     */
    @Bean
    public FlatFileItemWriter<Product> fileWriter() {
        return new FlatFileItemWriterBuilder<Product>()
                .name("productItemWriter")
                .shouldDeleteIfExists(true)
                .resource(new FileSystemResource("products.txt"))
                .lineAggregator(new PassThroughLineAggregator<>())
                .build();
    }

    /**
     * Создает композитный писатель для использования обоих писателей данных.
     *
     * @param dataSource источник данных для записи
     * @return CompositeItemWriter для использования обоих писателей данных
     */
    @Bean
    public CompositeItemWriter<Product> compositeItemWriter(DataSource dataSource) {
        CompositeItemWriter<Product> compositeWriter = new CompositeItemWriter<>();
        compositeWriter.setDelegates(Arrays.asList(writer(dataSource), fileWriter()));
        return compositeWriter;
    }

    /**
     * Создает Job для обработки данных.
     *
     * @param jobRepository репозиторий заданий
     * @param step1         шаг обработки данных
     * @return экземпляр Job для обработки данных
     */
    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
                .start(step1)
                .build();
    }

    /**
     * Создает шаг обработки данных для использования в работе.
     *
     * @param jobRepository       репозиторий работ
     * @param transactionManager  менеджер транзакций
     * @param reader              читатель данных
     * @param processor           обработчик данных
     * @param compositeItemWriter композитный писатель данных
     * @return экземпляр Step для обработки данных
     */
    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                      JdbcPagingItemReader<Product> reader, ItemProcessor<Product, Product> processor,
                      CompositeItemWriter<Product> compositeItemWriter) {
        return new StepBuilder("step1", jobRepository)
                .<Product, Product>chunk(100000, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(compositeItemWriter)
                .build();
    }
}