package cl.tuxpan.pruebaingreso.dtos;

import java.util.List;

/**
 * Response representation of an item.
 *
 * @param id         Item identifier
 * @param name       Item display name
 * @param idApuestas Ids of bets associated with this item
 */

public record ResItemDto(Integer id, String name, List<Integer> idApuestas) {}
