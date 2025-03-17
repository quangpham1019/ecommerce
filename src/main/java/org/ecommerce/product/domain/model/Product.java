package org.ecommerce.product.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private String name;
    private String description;

    private Long sellerId;

    @OneToOne
    @JoinColumn(name = "primary_variant_id", unique = true)
    private ProductVariant primaryVariant;

    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @ToString.Exclude
    private Set<Category> categories;


    public Product(String name, String description, ProductVariant primaryVariant,  Set<Category> categories) {
        this.name = name;
        this.description = description;
        this.primaryVariant = primaryVariant;
        this.categories = categories;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Product product = (Product) o;

        if (getId() != null && product.getId() != null)
            return Objects.equals(getId(), product.getId());

        return Objects.equals(getName(), product.getName());
    }

    @Override
    public final int hashCode() {
        if (this instanceof HibernateProxy)
            return ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode();

        if (getId() != null) return Objects.hash(getId());

        return Objects.hash(getName());
    }
}
