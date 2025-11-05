# @ConfigurationProperties() annotation in spring boot.

## 💡 What is `@ConfigurationProperties`?

It’s a Spring annotation used to **bind a group of related properties** (from `application.yml` or environment variables) into a **strongly-typed Java class**.
This makes configuration type-safe, testable, and organized.

---

## 🧱 Example: Production-ready usage

Let’s say you have some **JWT configuration** in `application.yml`:

```yaml
# src/main/resources/application.yml
app:
  jwt:
    secret: ${JWT_SECRET}
    expiration: 86400000  # 1 day in milliseconds
```

### 1️⃣ Create a Config Properties class

```java
package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    private String secret;
    private long expiration;
}
```

✅ **Key points:**

* `@ConfigurationProperties(prefix = "app.jwt")` → binds all properties under that prefix.
* Fields automatically map to property names.
* You get **type-safe access** (no magic strings or `@Value("${...}")`).

---

### 2️⃣ Inject and use it

You can inject this config wherever you need it:

```java
package com.example.controller;

import com.example.config.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tests")
public class TestJwtProperties {

    private final JwtProperties jwtProperties;

    @Autowired
    public TestJwtProperties(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @GetMapping("/jwt")
    public ResponseEntity<String> testJwtProperties() {
        return ResponseEntity.ok("JWT Properties: " + jwtProperties.getSecret() + " " + jwtProperties.getExpiration());
    }
}
```

## 🧩 Folder structure

```
src/main/java/com/gatiya/
  ├── config/
  │     ├── JwtProperties.java
  │     ├── ApplicationConfig.java
  │     └── DatabaseProperties.java
  └── controller/
        └── TestJwtProperties.java
```

Keep each externalized configuration (like JWT, DB, email, etc.) in its own `@ConfigurationProperties` class inside `config`. or you might
create `properties` package and put all the `@ConfigurationProperties` classes inside it. it would depend on you.

---

## ✅ Benefits in production

| Feature                                   | `@Value` | `@ConfigurationProperties` |
| ----------------------------------------- | -------- | -------------------------- |
| Type safety                               | ❌        | ✅                          |
| Grouped configs                           | ❌        | ✅                          |
| Easy to test/mock                         | ❌        | ✅                          |
| IDE autocompletion                        | ❌        | ✅                          |
| Supports complex structures (lists, maps) | ❌        | ✅                          |
