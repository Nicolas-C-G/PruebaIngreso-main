# Prueba Ingreso Tuxpan

![Minimum Java Version](https://img.shields.io/badge/java-21-blue?logo=oracle)
![Minimum Postgres Version](https://img.shields.io/badge/postgres-15.4-blue?logo=postgresql)
![Minimum Spring Boot Version](https://img.shields.io/badge/Spring_boot-3.2.0-blue?logo=springboot)

Esta es una prueba de ingreso para Tuxpan, esta busca evaluar capacidades de desarrollo y resolución de problemas de
Spring Boot y Java, debes resolverla siguiendo las instrucciones que se te entregaran a continuación.

Esta prueba se compone de varias partes que se deben resolver en orden, cada parte debería tener una dificultar superior
a la anterior, no es necesario resolver todos los ejercicios, dado que el tiempo es limitado, pero se recomienda resolver 
la mayor cantidad posible.

## Requisitos

- Java 21.
- IntelliJ IDEA Community.
- Docker y Docker Compose.

## Información adicional:

- La historia presentada a continuación sigue un formato de RolePlay, donde se te presentan los requerimientos como si
  fueras un desarrollador de Tuxpan, y tú debes resolverlos.
- En el proyecto se encuentra un archivo llamado `compose.yml` que contiene la configuración para levantar una
  base de datos Postgres. No se debe levantar de forma manual, el proyecto se encarga de levantarla. Solo debes tener
  instalada la última version de Docker y Docker Compose (En caso de problemas puedes levantar la base de datos con
  docker compose de forma manual).
- la arquitectura del proyecto es en capas, donde cada capa tiene una responsabilidad específica. Se pueden añadir capas
  si lo considera necesario o incluso migrarlo a una arquitectura hexagonal, pero se debe justificar el porqué.

### Instrucciones

- Abrir este proyecto en la IDE.
- Resolver los ejercicios en orden.
- Subir el proyecto a un repositorio de tu preferencia (Github, Gitlab, Bitbucket, etc).
- Enviar el link del repositorio a la persona que te envió esta prueba.

### Recomendaciones

- No se deberían instalar nuevas dependencias, pero si es necesario, se debe justificar el porqué.

## Historia

Como Tuxpan Software, poseemos muchos clientes y proyectos complejos. Uno de nuestros clientes es una casa
de subastas.

Ellos trabajan con una arquitectura de microservicios (solo 2 actualmente), este proyecto es uno de ellos, y se encarga
de recibir por medio de llamadas HTTP el usuario que desea apostar, el monto que desea apostar y el producto. Este
microservicio no cuenta con sistema de usuarios ni nada por el estilo, solo recibe la información y la almacena en una
base de datos.

``` Tienes libertad para modificar donde quieras, solo intenta mantener el orden. ```

### Parte 1

El cliente nos ha solicitado que se agregue un nuevo endpoint para obtener el monto total apostado por un usuario.

### Parte 2

El cliente nos informa que ha tenido problemas con su otro microservicio, el cual nos envia las solicitudes, lo cual ha
implicado que ha tenido que renovarlo; sin embargo, el nuevo microservicio no tiene todas las validaciones que tenía el
anterior. Debido a esto nos solicita que agreguemos las siguientes validaciones:

- El usuario no puede tener un nombre mayor a 50 caracteres.
- El usuario no puede tener un nombre menor a 5 caracteres.
- El monto no puede ser menor a 1000.
- El monto no puede ser mayor a 999999999.

```Eres libre de usar lo que quieras para validar, pero se valora la sencilles.```

### Parte 3

El cliente nos informa de un bajo performance que tiene el microservicio, por lo cual nos solicita que realicemos las
optimizaciones correspondientes. El método más afectado por el performance es el que obtiene el usuario ganador de una subasta.

### Parte 4

Se nos informa que hay subastas corruptas por hackers intentando colapsar la bdd, por lo cual nos solicitan que
periodicamente se ejecute un proceso CRON que elimine las apuestas de subastas abiertas donde el usuario contenga un
carácter especial.  (caracteres especiales son aquellos que no son letras ni números).

Se recomienda que sea cada 2 minutos.

```Eres libre de buscar la forma de ejecutar el proceso CRON.```

### Parte 5

Puedes armar un front sencillo en la libreria/framework que te acomode, que consuma los endpoints del microservicio y permita realizar las
apuestas y ver las apuestas realizadas.

```No es necesario que sea visualemente atractivo, pero si que sea funcional.```

### Parte 6

Agregar un nuevo docker-compose para que el proyecto pueda ser levantado y probado de forma sencilla, independiente de la IDE que se use,
junto con lo que agregamos en la parte 5.
```El docker-compose debe levantar la base de datos y el microservicio, bonus si le agregas el front.```

### Parte 7

Modificar el readme, haciendo un pequeño punteo de como se abordaron los puntos anteriores, y como levantar el proyecto,
pensando en que lo va a leer un revisor.

## Puntos bonus

- Mejoras no solicitadas.
- Documentación de código. (JavaDoc, etc).
- Documentación de API. (Swagger, Postman, etc).
- Pruebas unitarias. (JUnit, Mockito, etc).
