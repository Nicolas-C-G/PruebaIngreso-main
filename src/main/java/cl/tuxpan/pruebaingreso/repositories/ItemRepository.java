package cl.tuxpan.pruebaingreso.repositories;

import cl.tuxpan.pruebaingreso.models.ItemModel;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@code ItemModel} entities.
 * <p>
 * Provides CRUD operations and query derivation for items.
 */
public interface ItemRepository extends JpaRepository<ItemModel, Integer> {}
