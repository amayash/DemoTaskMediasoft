package com.mediasoft.warehouse.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aws")
@Getter
@Setter
public class AwsConfigurationProperties {
    private String path;
    private String accessKeyId;
    private String secretAccessKey;
    private String region;
    private String bucketName;
}
