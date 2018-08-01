package ru.gpb.rkk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;


@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@EnableDiscoveryClient
@RestController
@RefreshScope
public class Application {

    @Value("${service.versions}")
    public   String version;

    @RequestMapping("/")
    public String home() {

        return "Hello World " + version;
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);


    }
}
