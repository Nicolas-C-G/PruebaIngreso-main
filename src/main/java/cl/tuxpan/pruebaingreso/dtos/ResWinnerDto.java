package cl.tuxpan.pruebaingreso.dtos;

/**
 * Response with the current winning (highest) bet for an item.
 *
 * @param itemId        Identifier of the item
 * @param itemName      Name of the item
 * @param usuarioId     Identifier of the winning user
 * @param usuarioNombre Name of the winning user
 * @param montoApuesta  Amount of the winning bet
 */

public record ResWinnerDto(
    Integer itemId, String itemName, Integer usuarioId, String usuarioNombre, Integer montoApuesta) {}
