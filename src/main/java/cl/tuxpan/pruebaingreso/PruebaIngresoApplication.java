package cl.tuxpan.pruebaingreso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main entry point for the Prueba Ingreso Spring Boot application.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Bootstraps the Spring Boot application context</li>
 *   <li>Enables scheduled tasks via {@link EnableScheduling}</li>
 *   <li>Initializes the REST layer and data access layer</li>
 *   <li>Optionally integrates with Docker Compose to bring up dependencies
 *       such as a Postgres database, as configured in {@code compose.yaml}</li>
 * </ul>
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
