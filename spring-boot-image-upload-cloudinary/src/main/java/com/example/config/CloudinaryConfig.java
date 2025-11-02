package com.example.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Cloudinary Configuration */
@Configuration
public class CloudinaryConfig {
    private final Logger log = LoggerFactory.getLogger(CloudinaryConfig.class);

    private final CloudinaryProperties cloudinaryProperties;

    @Autowired
    public CloudinaryConfig(CloudinaryProperties cloudinaryProperties) {
        this.cloudinaryProperties = cloudinaryProperties;
    }

    @Bean
    public Cloudinary cloudinary() {
        log.info("Configuring Cloudinary");
        return new Cloudinary(
                ObjectUtils.asMap(
                        "cloud_name", cloudinaryProperties.getCloudName(),
                        "api_key", cloudinaryProperties.getApiKey(),
                        "api_secret", cloudinaryProperties.getApiSecret()
                )
        );
    }
}
