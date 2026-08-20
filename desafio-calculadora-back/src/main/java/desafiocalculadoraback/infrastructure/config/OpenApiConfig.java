package desafiocalculadoraback.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Calculadora de Empréstimos")
                        .version("1.0.0")
                        .description("API REST desenvolvida em Spring Boot com Arquitetura Hexagonal para simulação e gerenciamento de empréstimos e parcelamentos."));
    }
}
