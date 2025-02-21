# Lombok Cheatsheet

## 1. Getter & Setter
```java
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String username;
    private String email;
}
```
- `@Getter` generates getter methods.
- `@Setter` generates setter methods.

### Exclude a Field from Getter/Setter
```java
@Getter
@Setter
public class User {
    private String username;
    
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String password;
}
```
- `AccessLevel.NONE` prevents Lombok from generating a getter or setter.

---

## 2. @ToString
```java
import lombok.ToString;

@ToString
public class User {
    private String username;
    private String email;
}
```
- Generates a `toString()` method including all fields.

### Exclude Fields from toString()
```java
@ToString(exclude = "password")
public class User {
    private String username;
    private String password;
}
```

---

## 3. @EqualsAndHashCode
```java
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class User {
    private String username;
    private String email;
}
```
- Generates `equals()` and `hashCode()`.

### Exclude Fields
```java
@EqualsAndHashCode(exclude = "password")
public class User {
    private String username;
    private String password;
}
```

---

## 4. Constructors
```java
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor  // No-args constructor
@AllArgsConstructor // Constructor with all fields
@RequiredArgsConstructor // Constructor for final fields
public class User {
    private String username;
    private final String email;
}
```

---

## 5. @Data (Combination of Multiple Annotations)
```java
import lombok.Data;

@Data  // Combines @Getter, @Setter, @ToString, @EqualsAndHashCode, and @RequiredArgsConstructor
public class User {
    private String username;
    private String email;
}
```

---

## 6. @Builder (Fluent Builder Pattern)
```java
import lombok.Builder;

@Builder
public class User {
    private String username;
    private String email;
}

// Usage
User user = User.builder().username("john").email("john@example.com").build();
```

---

## 7. Logging with @Slf4j
```java
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerExample {
    public void logSomething() {
        log.info("Logging with Lombok!");
    }
}
```
- Adds a `static final Logger log` instance.

---

## 8. @Value (Immutable Class)
```java
import lombok.Value;

@Value
public class ImmutableUser {
    String username;
    String email;
}
```
- Makes the class immutable (all fields `final`, no setters, only getters).

---

## Summary Table
| Annotation | Description |
|------------|-------------|
| `@Getter` / `@Setter` | Generates getter and setter methods |
| `@ToString` | Generates `toString()` method |
| `@EqualsAndHashCode` | Generates `equals()` and `hashCode()` |
| `@NoArgsConstructor` | No-args constructor |
| `@AllArgsConstructor` | Constructor with all fields |
| `@RequiredArgsConstructor` | Constructor for final fields |
| `@Data` | Combines multiple Lombok annotations |
| `@Builder` | Implements Builder pattern |
| `@Slf4j` | Adds a logger instance |
| `@Value` | Creates an immutable class |

---

This cheat sheet provides a quick reference for Lombok annotations. 🚀