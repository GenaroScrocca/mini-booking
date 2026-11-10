# Mini-Booking

Mini-Booking es una aplicación web de reservas de alojamientos. Incluye un frontend para clientes, un panel administrativo y una API REST conectada a MySQL.

## Tecnologías

- Java 17
- Spring Boot
- Maven
- Spring Data JPA / Hibernate
- MySQL
- React
- Vite
- Axios
- JUnit 5
- Mockito

## Arquitectura

```text
React -> API REST -> Controller -> Service -> Repository -> MySQL
```

El backend mantiene separadas las capas de controladores, servicios, repositorios y entidades. El frontend consume la API mediante Axios.

## Requisitos previos

- Java 17
- Node.js y npm
- MySQL

## Base de datos

Crear una base de datos llamada `mini_booking`:

```sql
CREATE DATABASE mini_booking;
```

La conexión se configura en `src/main/resources/application.properties`. Antes de iniciar el backend, verificar que MySQL esté ejecutándose y que el usuario y la contraseña configurados correspondan al entorno local.

Al iniciar la aplicación se crean, si todavía no existen, los roles `CLIENTE` y `ADMIN`. El proceso es idempotente y no utiliza IDs fijos.

## Ejecutar el backend

Desde la raíz del proyecto, en Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

En Linux o macOS:

```bash
./mvnw spring-boot:run
```

El backend queda disponible en `http://localhost:8080`.

## Ejecutar el frontend

```bash
cd frontend
npm install
npm run dev
```

El frontend queda disponible en `http://localhost:5173`.

## URLs principales

- Frontend: `http://localhost:5173`
- Backend: `http://localhost:8080`
- Swagger/OpenAPI: `http://localhost:8080/swagger-ui/index.html`

## Funcionalidades

### Cliente

- Registro e inicio de sesión.
- Consulta y detalle de propiedades.
- Filtros por ciudad y categoría.
- Búsqueda de propiedades disponibles por fechas.
- Creación de reservas.
- Consulta de reservas propias.
- Modificación de fechas de una reserva.
- Cancelación de reservas.
- Cierre de sesión.

### Administración

- Panel administrativo.
- Alta, consulta, modificación y eliminación de propiedades.
- Alta, consulta, modificación y eliminación de categorías.
- Alta, consulta, modificación y eliminación de ciudades.

## Usuarios y roles

Los roles disponibles son:

- `CLIENTE`
- `ADMIN`

El registro público siempre crea usuarios con rol `CLIENTE`; el request no permite elegir el rol.

Para preparar una cuenta administrativa en el entorno local, primero registrar el usuario normalmente y luego promoverlo manualmente desde MySQL:

```sql
UPDATE usuarios
SET rol_id = (
    SELECT id FROM roles WHERE nombre = 'ADMIN'
)
WHERE email = 'email_del_usuario';
```

La consulta busca el rol por nombre para no depender de un ID fijo.

La autenticación actual utiliza contraseñas hasheadas con BCrypt. Es una implementación mínima para demostración: no incluye JWT, sesiones de backend ni protección real de endpoints por rol. El frontend conserva únicamente los datos públicos del usuario en `localStorage` y protege visualmente las rutas administrativas.

## Pruebas y verificaciones

Ejecutar los tests del backend desde la raíz:

```powershell
.\mvnw.cmd test
```

El proyecto cuenta actualmente con **23 tests backend aprobados**, que cubren autenticación y lógica relevante de productos, usuarios y reservas.

Verificaciones del frontend:

```bash
cd frontend
npm run build
npm run lint
```
