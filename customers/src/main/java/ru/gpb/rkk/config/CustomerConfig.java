package ru.gpb.rkk.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Service configuration. This example contains sample of refreshable part of config (customers.yml)
 * <p>
 * Config file of service storing in git repository with name like name of service. Automatically configuration
 * is put in Consul server via git2consul application
 */
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
