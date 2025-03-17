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
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private String warehouseName;
    private String warehouseAddress;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private Set<Inventory> inventorySet;

    public Warehouse(String warehouseName, String warehouseAddress) {
        this.warehouseName = warehouseName;
        this.warehouseAddress = warehouseAddress;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Warehouse other = (Warehouse) o;

        if (getId() != null && other.getId() != null)
            return Objects.equals(getId(), other.getId());

        return Objects.equals(getWarehouseName(), other.getWarehouseName())
                && Objects.equals(getWarehouseAddress(), other.getWarehouseAddress());
    }

    @Override
    public final int hashCode() {
        if (this instanceof HibernateProxy)
            return ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode();

        if (getId() != null) return Objects.hash(getId());

        return Objects.hash(getWarehouseName(), getWarehouseAddress());
    }
}
