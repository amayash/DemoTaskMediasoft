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
    String accessKeyId;
    String secretAccessKey;
    String region;
    String bucketName;
}
