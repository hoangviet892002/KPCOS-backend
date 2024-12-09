package com.CP.KPCOS.configuration;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@SecurityScheme(
        name = "Authorization",
        description = "API Key Authentication",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER
)

public class OpenAPIConfig {
    @Value("${spring.application.openApi.description}")
    private String appDescription;
    @Value("${spring.application.openApi.version}")
    private String appVersion;
    @Value("${spring.application.openApi.title}")
    private String appTitle;
    @Value("${spring.application.openApi.license.name}")
    private String appLicense;
    @Value("${spring.application.openApi.license.url}")
    private String appLicenseUrl;
    @Value("${spring.api.prefix}")
    private String apiPrefix;

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title(appTitle)
                        .version(appVersion)
                        .description(appDescription)
                        .license(new License().name(appLicense).url(appLicenseUrl)))
                .servers(List.of(new Server().url(apiPrefix)));

    }
}
