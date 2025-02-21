
**1️⃣ Use `@Getter` and `@Setter` Instead of `@Data` in JPA Entities**

🔹 `@Data` automatically generates `toString()`, `equals()`, and `hashCode()`, which can cause **Hibernate issues** due to lazy-loaded relationships.

🔹 **Best practice**: Use `@Getter` and `@Setter` individually.

✅ **Good Example (Recommended for JPA Entities)**

```java
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @ToString.Exclude // Prevents LazyInitializationException
    private List<OrderItem> items;
}
```

❌ **Bad Example (Using `@Data` on JPA Entity)**

```java
@Entity
@Data // ⚠️ Avoid using @Data with JPA entities
public class Order {
    @Id
    private Long id;
    @OneToMany(mappedBy = "order")
    private List<OrderItem> items; // 🚨 This can trigger lazy loading issues in `toString()`
}
```

---

**2️⃣ Use `@EqualsAndHashCode(onlyExplicitlyIncluded = true)` for Entities** 

🔹 By default, Lombok includes **all fields** in `equals()` and `hashCode()`, which can cause issues with Hibernate proxies and performance.
🔹 **Best practice**: Use only the **ID field** for equality checks.

✅ **Good Example**

```java
@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // ✅ Use only ID for equality checks
    private Long id;
    private String email;
}
```

❌ **Bad Example (Using Default `@EqualsAndHashCode`)**

```java
@Entity
@Data // ⚠️ Includes all fields in `equals()` and `hashCode()`, causing issues with proxies
public class User {
    @Id
    private Long id;
    private String email;
}
```

---

**3️⃣ Use `@Value` for Immutable DTOs** 

🔹 **DTOs (Data Transfer Objects)** should be immutable.
🔹 Use `@Value` instead of `@Data` for **read-only classes**.

✅ **Good Example (Immutable DTO)**

```java
@Value
public class UserDTO {
    String name;
    String email;
}
```

❌ **Bad Example (Using `@Data` for DTOs)**

```java
@Data // ⚠️ Generates setters, making it mutable
public class UserDTO {
    private String name;
    private String email;
}
```

---

**4️⃣ Use `@Builder` for Complex Object Creation** 

🔹 The `@Builder` annotation is useful for **constructing objects** with many optional fields.

✅ **Good Example**

```java
@Getter
@Builder
public class Product {
    private String name;
    private double price;
    private String category;
}
```

✅ **Usage**

```java
Product product = Product.builder()
    .name("Laptop")
    .price(999.99)
    .category("Electronics")
    .build();
```

❌ **Bad Example (Using Constructor for Many Fields)**

```java
Product product = new Product("Laptop", 999.99, "Electronics");
```

💡 **Builder improves readability** when dealing with many fields.

---

**5️⃣ Use `@Slf4j` for Logging Instead of Manually Declaring a Logger** 

🔹 `@Slf4j` (from Lombok) removes the need for manual logger declarations.

✅ **Good Example**

```java
@Service
@Slf4j
public class OrderService {
    public void processOrder(Long orderId) {
        log.info("Processing order: {}", orderId);
    }
}
```

❌ **Bad Example (Manually Declaring a Logger)**

```java
@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    public void processOrder(Long orderId) {
        log.info("Processing order: {}", orderId);
    }
}
```

---

**6️⃣ Use `@NoArgsConstructor` and `@AllArgsConstructor` for Entities** 

🔹 **JPA requires a no-argument constructor** for entity instantiation.

🔹 `@AllArgsConstructor` is helpful when you need a full-args constructor.

✅ **Good Example**

```java
@Entity
@Getter
@Setter
@NoArgsConstructor // ✅ Required by JPA
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
}
```

---

**7️⃣ Use `@RequiredArgsConstructor` for Dependency Injection in Services** 

🔹 Instead of manually writing constructors for **Spring components**, use `@RequiredArgsConstructor` for **dependency injection**.

✅ **Good Example**

```java
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository; // ✅ Injected via constructor
}
```

❌ **Bad Example (Manual Constructor for Dependency Injection)**

```java
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    public OrderService(OrderRepository orderRepository) { // ⚠️ Unnecessary boilerplate
        this.orderRepository = orderRepository;
    }
}
```

