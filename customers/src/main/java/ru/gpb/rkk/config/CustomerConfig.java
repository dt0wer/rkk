package ru.gpb.rkk.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
public class CustomerConfig {

    @Value("${service.versions}")
    public String version;

    @Bean
    public String getVersion() {
        return this.version;
    }
}
