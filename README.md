<div align="center">

# 🛒 ApiRest

**Backend de tienda con Java y Spring Boot**

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4-6DB33F?logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8-4479A1?logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/Auth-JWT-black?logo=jsonwebtokens&logoColor=white)

</div>

API que usa la app Android para que los usuarios se registren, inicien sesión y consulten el catálogo de categorías y productos con sus imágenes.

##  Funcionalidades

- Registro e inicio de sesión con token JWT.
-  Roles: `ADMIN`, `CLIENT` y `DRIVER`.
-  Categorías y productos con imágenes.
-  Papelera con borrado lógico: lo eliminado se oculta, no se borra, y un administrador puede restaurarlo.

##  Tecnologías

| Capa | Tecnología |
|---|---|
| Lenguaje | Java 17 |
| Framework | Spring Boot 4 |
| Seguridad | Spring Security + JWT |
| Datos | Spring Data JPA + MySQL |

## ⚙ Cómo funciona

```
App Android ──► Spring Security (valida JWT) ──► Controller ──► Service ──► Repository ──► MySQL
```

Cada petición lleva el header `Authorization: Bearer <token>`. Las imágenes se guardan en `uploads/` y se sirven en `/uploads/**`.



##  Ejecutarlo

1. Crear la base de datos en MySQL:
   ```sql
   CREATE DATABASE apirest_db;
   ```
2. Copiar `src/main/resources/application.yml.example` a `application.yml` y completar la conexión a MySQL y `jwt.secret`.
3. Iniciar:
   ```bash
   ./mvnw spring-boot:run
   ```

Las tablas se crean solas al arrancar.
