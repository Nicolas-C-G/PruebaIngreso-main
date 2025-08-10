package cl.tuxpan.pruebaingreso.repositories;

import cl.tuxpan.pruebaingreso.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@code UsuarioModel} entities.
 */

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {
  /**
   * Finds a user by exact name match.
   *
   * @param name: user name
   * @return optional user with that name
   */
  Optional<UsuarioModel> findByName(String name);
}
