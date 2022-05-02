package com.email.microservice.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal class OpenApiConfig {

    @Bean
    internal fun openApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Email Microservice API")
                    .description("A RESTFull API for an email microservice")
                    .version("v1.0.0")
                    .contact(
                        Contact()
                            .name("Panagiotis Gkavogiannis")
                            .url("https://www.linkedin.com/in/panagiotis-gkavogiannis/")
                            .email("pgkavogiannis@mailnator.com")
                    )
                    .termsOfService("TOC")
                    .license(License().name("MIT License").url("https://choosealicense.com/licenses/mit/"))
            )
    }
}