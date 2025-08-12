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

Resumen de lo realizado para este paso:
- Se procedió a crear y configurar una maquina virtual Ubuntu 24 en Virtual box para poder realizar la prueba en un ambiente aislado. 
   Se instaló Java21, IntelliJ IDEA Community y Docker. 
- Se descargo el código en la VM y se abrió con IntelliJ IDEA.
- Desde el IDE se creo el repositorio en GitHub y las ramas main y develop.
- Se probó que el código funcionará.
- Se buscó entender el código por lo cual se hizo un analisis de las funciones que habian y se generó documentación al respecto (commit: 9c5e165 develop)
- Una vez interiorizado de lo que habia que hacer, se procedio a crear un nuevo endpoint que retornará el monto total apostado por el usuario.
- Se creó un archivo de pruebas para testear esta funcionalidad (FrontControllerIT.java) 
- El resultado de esto lo encontrará en el commit 9d35ebb de la rama develop.

### Parte 2

El cliente nos informa que ha tenido problemas con su otro microservicio, el cual nos envia las solicitudes, lo cual ha
implicado que ha tenido que renovarlo; sin embargo, el nuevo microservicio no tiene todas las validaciones que tenía el
anterior. Debido a esto nos solicita que agreguemos las siguientes validaciones:

- El usuario no puede tener un nombre mayor a 50 caracteres.
- El usuario no puede tener un nombre menor a 5 caracteres.
- El monto no puede ser menor a 1000.
- El monto no puede ser mayor a 999999999.

```Eres libre de usar lo que quieras para validar, pero se valora la sencilles.```

Resumen de lo realizado para este paso:

- Para esta parte hice una pequeña investigación en internet y encontré varias opciones que la Bean Validation on DTO era la más 
   limpia y sencilla de implementar y además en las dependencias ya estába incluido 'org.springframework.boot:spring-boot-starter-validation'.
   Ahora bien, si alguien por alguna razon hiciera yn bypass de la API, la validación quedaría inservible.
   
   Curls para testear
       userName corto
        curl -X POST "http://localhost:8080/api/v1/apuesta" \
             -H "Content-Type: application/json" \
             -d '{"itemId":1,"usuarioNombre":"abcd","montoApuesta":1000}'


       Monto muy bajo
        curl -X POST "http://localhost:8080/api/v1/apuesta" \
             -H "Content-Type: application/json" \
             -d '{"itemId":1,"usuarioNombre":"validName","montoApuesta":999}'

- Cabe destacar que el archivo FrontControllerIT.java recibió una actualizacion con los respectivos test para esta funcionalidad (commit: 8ba8aec develop).

### Parte 3

El cliente nos informa de un bajo performance que tiene el microservicio, por lo cual nos solicita que realicemos las
optimizaciones correspondientes. El método más afectado por el performance es el que obtiene el usuario ganador de una subasta.

Resumen de lo realizado para este paso

- Para poder tener un punto de partida, tome como kpi la latencia del endpoint, para poder medir aquello tuve que usar actuator y data-jpa los cuales ya se encontraban dentro de las  dependencias.

- Para poder medir el estado actual del endpoint entorno al kpi, se tomo el estado basal usando los siguientes curls:
    1. for i in {1..20}; do curl -s -o /dev/null "http://localhost:8080/api/v1/winner/1"; done

    2. curl -s "http://localhost:8080/actuator/metrics/http.server.requests?tag=uri:/api/v1/winner/%7BitemId%7D&tag=method:GET&tag=status:200" \
    | jq '{count: (.measurements[] | select(.statistic=="COUNT").value),
           total: (.measurements[] | select(.statistic=="TOTAL_TIME").value),
           max:   (.measurements[] | select(.statistic=="MAX").value)}
          | {count, avg_ms: ((.total/.count)*1000), max_ms: (.max*1000)}'

  Donde el primero solo busca activar el endpoint y el segundo entrega las estadisticas relacionadas a la latencia en cada activación.
  
  El resultado basal fue de: Winner endpoint perf: runs=50, bets=1000, avg=4.48 ms, p95=8.27 ms, max=35.73 ms

- Una vez ya contaba con un estado basal me puse a investigar respecto a que cosas podrian estar aumentando la latencia o como podría reducirla. 
  Dado que el endpoint realiza consultas a una DB uno de los problemas de un bajo performance podría ser la falta de un Index, or lo que se procedió a
  crear uno en el archivo V01_01__add_winner_index.sql. 

- Una vez realizado los cambios, se procedio a probarlos nuevamente tanto con los curls y los test y se obtubo como resultado: Winner endpoint perf: runs=50, bets=1000, avg=2.41 ms, p95=2.89 ms, max=4.13 ms. Lo que muestra una reduccion significativa en la latencia.

Nota: Cabe destacar que los test del archivo FrontControllerIT.java, no usan la misma db que se usaría si se ejecuta el PruebaDeIngresoApplication.java. 

### Parte 4

Se nos informa que hay subastas corruptas por hackers intentando colapsar la bdd, por lo cual nos solicitan que
periodicamente se ejecute un proceso CRON que elimine las apuestas de subastas abiertas donde el usuario contenga un
carácter especial.  (caracteres especiales son aquellos que no son letras ni números).

Se recomienda que sea cada 2 minutos.

```Eres libre de buscar la forma de ejecutar el proceso CRON.```

Resumen de lo realizado para este paso

- Para crear el cron en java y verificar su funcionamiento, fue necesario crear el AdminController.java ya que requería de una forma de ver todas las apuestas
  realizadas en un momento determinado.
  
- En el archivo BetCleanupJob.java, encontrará la rutina que se ejecuta cada dos minutos con la finalidad de eliminar todos los usuarios que contengan
  caracteres especiales.
  
- Una de las grandes dificultades, fue el que al realizar la query en postgress esta no es full compatible con la base de datos que levanta el
  archivo de testeo. Por lo que se procedió a crear un shell script con el proposito de revisar el correcto funcionamiento del cron (test_special_char_cleanup.sh)

  commit: 9eeae0a develop

### Parte 5

Puedes armar un front sencillo en la libreria/framework que te acomode, que consuma los endpoints del microservicio y permita realizar las
apuestas y ver las apuestas realizadas.

```No es necesario que sea visualemente atractivo, pero si que sea funcional.```

Resumen de lo realizado para este paso

- Dado que no habia un requrimiento especifico de diseño y framework se optó por lo más sensillo que fue crear una carpeta en static en resources, dentro de la cual se crearón dos vistas una para el usuario y otra para un administrador. Cabe destacar que fue necesario crear nuevos endpoints por temas de visualización de datos y para verificar que todo funcionará como se espera.

Commit: 4da9a1c and 6106bdc develop

### Parte 6

Agregar un nuevo docker-compose para que el proyecto pueda ser levantado y probado de forma sencilla, independiente de la IDE que se use,
junto con lo que agregamos en la parte 5.
```El docker-compose debe levantar la base de datos y el microservicio, bonus si le agregas el front.```

Resumen de lo realizado para este paso

- Se creo un archivo Dockerfile y un docker-compose.yml tal como se solicita.
- Para levantar los servicios se recomiendo descargar el archivo del github: https://github.com/Nicolas-C-G/PruebaIngreso-main.git entrar a la carpeta PruebaIngreso-main y
  ejacutar "docker compose -f docker-compose.yml up -d --build".

- Una vez que los servicios esten arriba, podrá acceder a la interfaz de usuario atraves de http://localhost:8080/user.html y a la interfaz de administrador atraves de      
  http://localhost:8080/index.html.
  
Nota: Es importante tener instalado docker en la maquina donde se pretende levantar los servicios.

### Parte 7

Modificar el readme, haciendo un pequeño punteo de como se abordaron los puntos anteriores, y como levantar el proyecto,
pensando en que lo va a leer un revisor.

Importante: se recomienda abrir la vista de administrador y crear items primero y despues usar la del usuario para apostar por los items ya creados.

## Puntos bonus

- Mejoras no solicitadas.
- Documentación de código. (JavaDoc, etc).
- Documentación de API. (Swagger, Postman, etc).
- Pruebas unitarias. (JUnit, Mockito, etc).

Extras: 
- Para generar los JavaDoc, ejecutar ./gradlew javadoc dentro de la carpeta PruebaIngreso-main.
- En la parte 4 también se podría agregar alguna especie de limitador de request asociados a una ip, evitando que una maquina haga mas de un request por seg por ejemplo.
- La seguridad de los endpoint puede mejorarse usando hash o encriptaciones.

