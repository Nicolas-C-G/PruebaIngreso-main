package cl.tuxpan.pruebaingreso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Entry point for the Spring Boot application.
 *
 * Boots the web context, sets up the REST layer, and (via Spring Boot's Docker
 * Compose integration) can bring up the Postgres service defined in {@code compose.yaml}.
 */

@EnableScheduling
@SpringBootApplication
public class PruebaIngresoApplication {

    /**
     * Starts the Spring Boot application.
     *
     * @param args standard JVM command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(PruebaIngresoApplication.class, args);
    }

}
