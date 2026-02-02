package com.insurance.auto.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Auto Insurance API")
                        .description("자동차 보험 가입/심사/승인 프로세스 API 명세서")
                        .version("v1.0.0"));
    }
}
