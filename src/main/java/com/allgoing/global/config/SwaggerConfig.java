package com.allgoing.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.servers.Server;

@OpenAPIDefinition(servers = {@io.swagger.v3.oas.annotations.servers.Server(url = "http://ec2-3-37-6-188.ap-northeast-2.compute.amazonaws.com:8080",
        description = "Default Server URL")})

@Configuration
public class SwaggerConfig {

    @Value("${api.server.url}")
    private String serverUrl;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(info)
                .addServersItem(new Server().url(serverUrl));
    }

    Info info = new Info().title("ALLGOING Backend APIS").version("0.0.1").description(
            "<h3>ALLGOING Backend APIS</h3>");

}

