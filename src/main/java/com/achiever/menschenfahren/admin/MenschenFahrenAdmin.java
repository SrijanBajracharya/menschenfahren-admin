package com.achiever.menschenfahren.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;

import com.achiever.menschenfahren.admin.config.MenschenFahrenConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class, ErrorMvcAutoConfiguration.class })
@PropertySource("classpath:application-achiever.menschenfahren.properties")
@EnableConfigurationProperties(MenschenFahrenConfig.class)
@Slf4j
public class MenschenFahrenAdmin extends SpringBootServletInitializer {

    public static void main(final String[] args) {
        SpringApplication.run(MenschenFahrenAdmin.class, args);
    }

}
