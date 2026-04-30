package com.bytecoach.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("ByteCoach API")
                .description("ByteCoach MVP backend API")
                .version("v1.0.0")
                .contact(new Contact().name("ByteCoach")));
    }
}

