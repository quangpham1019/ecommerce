package org.ecommerce;

import org.ecommerce.product.infrastructure.config.ProductContextConfig;
import org.ecommerce.user.infrastructure.config.UserContextConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({UserContextConfig.class, ProductContextConfig.class})
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

}
