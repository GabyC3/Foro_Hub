# Foro_Hub
Foro_Hub es una API REST basada en Spring Boot. Este proyecto utiliza un sistema seguro para usuarios, topicos, cursos y respuestas, donde se puedan realizar consultas y modificaciones.

Se centrarse específicamente en los tópicos, y debe permitir a los usuarios:

- Crear un nuevo tópico

- Mostrar todos los tópicos creados

- Mostrar un tópico específico

- Actualizar un tópico

- Eliminar un tópico

Esta API REST cuenta con las siguientes funcionalidades:

- API con rutas implementadas siguiendo las mejores prácticas del modelo REST.

- Validaciones realizadas según reglas de negocio.

- Implementación de una base de datos para la persistencia de la información.
- Servicio de autenticación/autorización para restringir el acceso a la información.


##Antes de ejecutar el programa:

Esta aplicación utiliza una base de datos, por lo que se debe crear una base de datos local y configurar el archivo src/main/resources/application.properties. En el archivo hay que reemplazar lo siguiente:

{DB_NAME}: Aqui tiene que ingresar el nombre de la base de datos creada.

{DB_HOST}: el nombre del host de la base de datos.

{DB_USERNAME}: el nombre del usuario de la base datos que utiliza.

{DB_PASSWORD}: contraseña asignada de tu cuenta para ingresar a la base de datos.


###Tecnologías Utilizadas
- Java JDK 17
- Maven: versión 4 en adelante
- Spring Boot: versión 3.2.3
- IDE (Entorno de desenvolvimento integrado) IntelliJ IDEA
- Framework Principal: Spring Boot
- Seguridad: Spring Security, JWT
- Acceso a Datos: Spring Data JPA, Hibernate
- Base de Datos: MySQL
- Documentación: SpringDoc OpenAPI (Swagger)
