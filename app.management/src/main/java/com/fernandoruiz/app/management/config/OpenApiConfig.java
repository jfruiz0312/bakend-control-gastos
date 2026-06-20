package com.fernandoruiz.app.management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI controlGastosOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Control de Gastos Operativos e Inventario")
                        .description("API para gestionar productos, compras, gastos operativos, ventas y reportes financieros.")
                        .version("v1")
                        .contact(new Contact().name("Control Gastos"))
                        .license(new License().name("Uso interno")));
    }
}
