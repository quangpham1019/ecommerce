package org.ecommerce.user.infrastructure.security;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
            title = "E-Commerce API",
            version = "1.0",
            description = "API documentation for the e-commerce application")
//        ,security = @SecurityRequirement(name = "Bearer Authentication")
)
//@SecurityScheme(
//        name = "Bearer Authentication",
//        type = SecuritySchemeType.HTTP,
//        scheme = "bearer",
//        bearerFormat = "JWT"
//)
public class SwaggerConfig {
    // This configuration will automatically include cookie-based parameter globally
}
