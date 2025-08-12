package cl.tuxpan.pruebaingreso.dtos;

/**
 * Data transfer object representing the details of a bet (apuesta).
 * <p>
 * This DTO is typically returned when listing or retrieving bets,
 * providing both the identifying information and associated metadata
 * about the bet and its related item.
 *
 * @param id        the unique identifier of the bet
 * @param itemId    the unique identifier of the associated item
 * @param itemName  the display name of the associated item
 * @param amount    the wagered amount for the bet
 */
public record ResApuestaDetailDto(Integer id, Integer itemId, String itemName, Integer amount) {}
