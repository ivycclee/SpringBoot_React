package com.example.assignmenttwo_starter.application;

import com.example.assignmenttwo_starter.repository.CustomerRepository;
import com.example.assignmenttwo_starter.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan("com.example.assignmenttwo_starter.model")
@ComponentScan({"com.example.assignmenttwo_starter.controller", "com.example.assignmenttwo_starter.service"})
@EnableJpaRepositories("com.example.assignmenttwo_starter.repository")
@Configuration
@EnableWebMvc
@EnableCaching
public class AssignmentTwoStarterApplication implements WebMvcConfigurer {
    @Autowired
    private CustomerRepository cRepo;

    @Autowired
    private OrderRepository oRepo;

    public static void main(String[] args) {
        SpringApplication.run(AssignmentTwoStarterApplication.class, args);
    }

    //this causes error for pdf
//    @Bean
//    public WebMvcConfigurer customConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
//                configurer.defaultContentType(MediaType.APPLICATION_JSON);
//            }
//        };
//    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD")
                .allowCredentials(true);
    }
}
