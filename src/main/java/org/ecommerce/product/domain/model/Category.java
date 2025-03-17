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
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private String categoryName;
    private String categoryDescription;

    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
    @ToString.Exclude
    private Set<Product> products;

    public Category(String categoryName, String categoryDescription) {
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Category category = (Category) o;

        if (getId() != null && category.getId() != null)
            return Objects.equals(getId(), category.getId());

        return Objects.equals(getCategoryName(), category.getCategoryName());
    }

    @Override
    public final int hashCode() {
        if (this instanceof HibernateProxy)
            return ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode();

        if (getId() != null) return Objects.hash(getId());

        return Objects.hash(getCategoryName());
    }
}
