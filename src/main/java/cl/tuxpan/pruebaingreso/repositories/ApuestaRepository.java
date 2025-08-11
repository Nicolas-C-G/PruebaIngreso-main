package cl.tuxpan.pruebaingreso.repositories;

import cl.tuxpan.pruebaingreso.models.ApuestaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
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

  /**
   * Calculates the total amount of all bets placed by a specific user.
   *
   * @param userId: The unique identifier of the user whose total bet amount is being queried
   * @return The sum of all bet amounts placed by the given user, or {@code 0} if none exist
   */

  @Query("""
           select coalesce(sum(a.amount), 0)
           from ApuestaModel a
           where a.user.id = :userId
           """)
  BigDecimal sumAmountByUserId(@Param("userId") Integer userId);
}
