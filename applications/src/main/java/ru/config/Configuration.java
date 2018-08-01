package ru.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @LoadBalanced
    @Bean
    public RestTemplate loadbalancedRestTemplate() {
        return new RestTemplate();
    }
}
