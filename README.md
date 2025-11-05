# 🩺 Microservicio de Gestión de Pacientes y Turnos Médicos

## 🧠 Descripción General
Sistema distribuido para la **gestión de pacientes y turnos médicos**, diseñado bajo arquitectura de **microservicios** utilizando **Spring Cloud**.  
Permite registrar pacientes, administrar sus turnos y consultar información médica mediante una **API REST** moderna, escalable y mantenible.

---

## ⚙️ Tecnologías Utilizadas

| Componente | Tecnología / Versión |
|-------------|----------------------|
| **Lenguaje** | Java 21 |
| **Framework principal** | Spring Boot 3.4.2 |
| **Arquitectura distribuida** | Spring Cloud 2024.0.0 |
| **Base de datos** | MySQL |
| **ORM** | JPA / Hibernate |
| **Pruebas unitarias e integración** | JUnit 5, Mockito, MockMvc |
| **Cobertura de código** | JaCoCo (82.2%) |
| **Calidad de código** | SonarQube (análisis local) |
| **Balanceo / resiliencia** | Resilience4j |
| **Comunicación entre servicios** | OpenFeign |
| **Descubrimiento de servicios** | Eureka Server |
| **Ruteo de peticiones** | Spring Cloud API Gateway |

---

## 🧩 Arquitectura del Sistema

El sistema está compuesto por múltiples microservicios registrados en **Eureka Server**, comunicados mediante **Feign Client** y gestionados por un **API Gateway**.  
El **Config Server** centraliza las configuraciones de todos los servicios.

```bash

                   ┌──────────────────────┐
                   │   Config Server      │
                   └─────────┬────────────┘
                             │
                             ▼
                    ┌────────────────┐
                    │ Eureka Server  │
                    └──────┬─────────┘
                           │
         ┌─────────────────┼──────────────────┐
         │                 │                  │
         ▼                 ▼                  ▼
 ┌───────────────┐  ┌───────────────┐  ┌───────────────────┐
 │ Microservicio │  │ Microservicio │  │     API Gateway    │
 │   Pacientes   │⇄ │    Turnos     │  │ (Spring Cloud GW) │
 └───────────────┘  └───────────────┘  └───────────────────┘
       ▲                     │
       │ (Feign Client)      │
       └─────────────────────┘

```
----

Cada servicio expone su propio conjunto de endpoints REST y se comunica mediante Feign Client, utilizando el número de documento del paciente como identificador compartido.
microservicio-consultorio/

│
├── pacientes/                  # Microservicio de pacientes
│   ├── src/main/java/...       # Código fuente principal
│   ├── src/test/java/...       # Pruebas unitarias e integración
│   ├── resources/application.yml
│   └── colección_postman.json  # Colección de pruebas
│
├── turnos/                     # Microservicio de turnos
│   └── colección_postman.json
│
├── api-gateway/
│   └── colección_postman.json
│
├── eureka-server/
│
└── config-server/

🧾 Funcionalidades Principales
🔹 Microservicio de Pacientes

Registrar nuevos pacientes

Consultar todos los pacientes o por ID

Buscar por número de documento

Editar y eliminar pacientes

Endpoints principales:

Método	Endpoint	Descripción
POST	/pacientes/crear	Crea un nuevo paciente
GET	/pacientes/traer	Lista todos los pacientes
GET	/pacientes/traer/{id}	Consulta un paciente por ID
GET	/pacientes/traer/documento/{numDocumento}	Consulta por documento
PUT	/pacientes/editar/{id}	Actualiza los datos de un paciente
DELETE	/pacientes/eliminar/{id}	Elimina un paciente

📘 En la carpeta raíz de cada microservicio se incluye la colección de Postman para realizar las pruebas de forma inmediata.

🧪 Pruebas y Calidad de Código

Pruebas unitarias implementadas en ambos microservicios con JUnit 5, Mockito y MockMvc.
Cobertura total: 82.2%, medida con JaCoCo.
Análisis de calidad: ejecutado con SonarQube local para garantizar buenas prácticas, métricas de mantenibilidad y detección temprana de bugs y code smells.

🚀 Ejecución del Proyecto (sin Docker)
Clonar el repositorio:

git clone https://github.com/JonatanLombo/microservicio-consultorio.git

Iniciar los servicios en el siguiente orden:

1. Config Server
cd config-server
mvn spring-boot:run

2. Eureka Server
cd ../eureka-server
mvn spring-boot:run

3. Microservicios
cd ../pacientes
mvn spring-boot:run

cd ../turnos
mvn spring-boot:run

4. API Gateway
cd ../api-gateway
mvn spring-boot:run


Acceder al panel de Eureka:
http://localhost:8761
Probar los endpoints desde Postman o el API Gateway.

✍️ Autor
Jonatan Lombo
       

