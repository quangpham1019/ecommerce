package org.ecommerce.product.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.repository.cdi.Eager;

import java.util.HashSet;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "primary_variant_id", unique = true)
    @JsonIgnore
    @ToString.Exclude
    private ProductVariant primaryVariant;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private Set<ProductVariant> productVariants = new HashSet<>();

    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();


    public Product(Long sellerId, String name, String description, ProductVariant primaryVariant,  Set<Category> categories) {
        this.sellerId = sellerId;
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
