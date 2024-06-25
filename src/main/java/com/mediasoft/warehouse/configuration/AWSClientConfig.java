package com.mediasoft.warehouse.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.mediasoft.warehouse.configuration.properties.AwsConfigurationProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для создания клиента Amazon S3.
 */
@Configuration
@Getter
@RequiredArgsConstructor
public class AWSClientConfig {
    private final AwsConfigurationProperties awsConfigurationProperties;

    /**
     * Создает и настраивает экземпляр клиента Amazon S3.
     *
     * @return настроенный клиент Amazon S3
     */
    @Bean
    public AmazonS3 s3Client() {
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        awsConfigurationProperties.getPath(),
                        awsConfigurationProperties.getRegion()))
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(awsConfigurationProperties.getAccessKeyId(),
                                awsConfigurationProperties.getSecretAccessKey())))
                .build();
    }
}
