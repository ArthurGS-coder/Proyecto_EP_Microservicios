# Tienda virtual con microservicios (Gestión de pedidos y catálogo)

Proyecto integrador del curso Arquitectura de Microservicios (Universidad Autónoma del Perú, 2026-2). Separamos una tienda virtual en dos servicios hechos con Java y Spring Boot:

- **order-service** (puerto 8081): crea y consulta pedidos.
- **catalog-service** (puerto 8082): publica el catálogo de productos.

Cada servicio tiene su propia base de datos H2 y se comunican solo por REST. Para crear un pedido, el order-service consulta `GET /catalog` del catalog-service y comprueba que los productos existan.

## Requisitos previos

- JDK 17
- Maven
- Git

Para comprobar que están instalados:

```bash
java -version
mvn -version
git --version
```

## Clonar el repositorio

```bash
git clone https://github.com/ArthurGS-coder/Proyecto_EP_Microservicios.git
cd Proyecto_EP_Microservicios
```

La carpeta tiene un proyecto Maven por servicio: `order-service` y `catalog-service`.

## Compilar y ejecutar los servicios

Hay que levantarlos en este orden, cada uno en su propia terminal, porque el order-service necesita que el catálogo esté arriba para validar los productos.

**1. Compilar ambos servicios** (una sola vez):

```bash
cd catalog-service
mvn clean package
cd ../order-service
mvn clean package
```

**2. Iniciar primero el catalog-service (puerto 8082):**

```bash
cd catalog-service
mvn spring-boot:run
```

Sus datos iniciales se cargan solos al arrancar.

**3. Iniciar el order-service (puerto 8081)**, en otra terminal:

```bash
cd order-service
mvn spring-boot:run
```

## Probar los endpoints

Podemos probarlos con Postman o con curl. Los tres endpoints son:

| Servicio | Método y ruta | Qué hace |
| --- | --- | --- |
| catalog-service | `GET http://localhost:8082/catalog` | Lista los productos del catálogo |
| order-service | `POST http://localhost:8081/orders` | Crea un pedido |
| order-service | `GET http://localhost:8081/orders/{id}` | Consulta un pedido por su id |

**Listar el catálogo:**

```bash
curl http://localhost:8082/catalog
```

**Crear un pedido:**

```bash
curl -X POST http://localhost:8081/orders \
  -H "Content-Type: application/json" \
  -d '{"items":[{"productId":1,"quantity":2,"unitPrice":50.00},{"productId":2,"quantity":1,"unitPrice":30.00}]}'
```

Responde `201 Created` con el id, el estado y el total:

```json
{
  "id": 1,
  "status": "PENDIENTE",
  "total": 130.0
}
```

**Consultar el pedido creado:**

```bash
curl http://localhost:8081/orders/1
```

**Caso inválido (pedido sin productos):**

```bash
curl -X POST http://localhost:8081/orders \
  -H "Content-Type: application/json" \
  -d '{"items":[]}'
```

Responde `400 Bad Request`:

```json
{
  "error": "El pedido debe tener al menos un producto"
}
```

Si el catalog-service no está levantado, `POST /orders` responde `503`.

> En Windows con CMD, las comillas del JSON de curl dan problemas. Ahí conviene usar Postman o guardar el JSON en un archivo y enviarlo con `-d @pedido.json`.

## Ejecutar las pruebas automatizadas

Dentro de la carpeta de cada servicio:

```bash
cd order-service
mvn test
```

```bash
cd catalog-service
mvn test
```

Las pruebas del order-service simulan el catálogo con Mockito, así que no hace falta tener el catalog-service corriendo. Maven deja los reportes en `target/surefire-reports`.

## Integración continua

El pipeline está en `.github/workflows/ci.yml` y se ejecuta en GitHub Actions. Valida que ambos servicios compilen y empaqueten sin errores con `mvn clean package -DskipTests`. Las pruebas las ejecutamos en local con `mvn test`.

## Integrantes

- Arturo Alberto Garamendi Sanchez
- Jose Luis Nicolas Estrada
- Adrian Josue Lulo Chumbipuma