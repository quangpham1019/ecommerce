package org.ecommerce.product.domain.model.value_objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Currency;

@Embeddable
@Getter
public class Price {

    @Column(name = "price")
    private final BigDecimal amount;
    private final String currency;

    public Price() {
        this.amount = BigDecimal.ZERO;
        this.currency = "";
    }

    public Price(BigDecimal amount, String currency) {
        isValidPrice(amount, currency);
        this.amount = amount;
        this.currency = currency;
    }

    void isValidPrice(BigDecimal amount, String currency) {
        if (amount.compareTo(BigDecimal.ZERO) >= 0) {
            Currency.getInstance(currency);
        } else {
            throw new IllegalArgumentException("Price must be positive");
        }
    }
}
