package cl.tuxpan.pruebaingreso.repositories;

import cl.tuxpan.pruebaingreso.models.ApuestaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

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
   * Finds the highest bet for the given item using a Spring Data derived query method.
   * <p>
   * This method avoids database-specific {@code LIMIT} syntax, making it more portable.
   *
   * @param id the identifier of the item
   * @return an {@link Optional} containing the highest {@link ApuestaModel} for the item,
   *         or empty if the item has no bets
   */
  Optional<ApuestaModel> findTopByItem_IdOrderByAmountDesc(Integer id);

  /**
   * Calculates the total amount of all bets placed by a specific user.
   *
   * @param userId: The unique identifier of the user whose total bet amount is being queried
   * @return The sum of all bet amounts placed by the given user, or {@code 0} if none exist
   */

  @Query("""
       select sum(a.amount)
       from ApuestaModel a
       where a.usuario.id = :userId
       """)
  BigDecimal sumAmountByUserId(@Param("userId") Integer userId);

  /**
   * Deletes all bets placed by users whose usernames contain special characters.
   * <p>
   * This uses a native SQL query with a regular expression match to identify
   * invalid usernames and remove their associated bets.
   *
   * @return the number of bets deleted
   */
  @Modifying
  @Query(value = """
    DELETE FROM subasta_apuesta a
    USING subasta_usuario u
    WHERE a.apuesta_usuario_id = u.usuario_id
      AND u.usuario_nombre ~ '[^A-Za-z0-9 ]'
    """, nativeQuery = true)
  int deleteBetsWithSpecialCharUsernames();

  /**
   * Retrieves all bets placed by a given user.
   *
   * @param userId the identifier of the user
   * @return a list of {@link ApuestaModel} placed by the specified user
   */
  List<ApuestaModel> findByUsuario_Id(Integer userId);

}
