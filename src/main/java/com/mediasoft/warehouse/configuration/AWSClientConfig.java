package com.mediasoft.warehouse.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.mediasoft.warehouse.configuration.properties.AwsConfigurationProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@RequiredArgsConstructor
public class AWSClientConfig {
    private final AwsConfigurationProperties awsConfigurationProperties;

    //    @Bean
//    public S3Client s3Client() {
//        return S3Client.builder()
//                .region(Region.of(awsConfigurationProperties.getRegion()))
//                .credentialsProvider(StaticCredentialsProvider.create(
//                        AwsBasicCredentials.create(
//                                awsConfigurationProperties.getAccessKeyId(),
//                                awsConfigurationProperties.getSecretAccessKey()
//                        )))
//                .build();
//    }
    @Bean
    public AmazonS3 s3Client() {
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration(
                        "s3.localhost.localstack.cloud:4566",
                        awsConfigurationProperties.getRegion()))
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(awsConfigurationProperties.getAccessKeyId(),
                                awsConfigurationProperties.getSecretAccessKey())))
                .build();
    }
}
