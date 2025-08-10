package cl.tuxpan.pruebaingreso.repositories;

import cl.tuxpan.pruebaingreso.models.ApuestaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@code ApuestaModel} entities (bets).
 */

public interface ApuestaRepository extends JpaRepository<ApuestaModel, Integer> {
  /**
   * Finds the highest bet for the given item.
   *
   * @param id: Item identifier
   * @return Optional highest bet; empty if the item has no bets
   */
  @Query("select p from ApuestaModel p where p.item.id = ?1 order by p.amount DESC LIMIT 1")
  Optional<ApuestaModel> findMaxBid(Integer id);
}
