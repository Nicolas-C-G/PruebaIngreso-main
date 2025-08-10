package cl.tuxpan.pruebaingreso.dtos;

/**
 * Request payload for placing a new bet (apuesta).
 *
 * @param itemId        Identifier of the item to bet on
 * @param usuarioNombre Name of the user placing the bet
 * @param montoApuesta  Amount of the bet
 */

public record ReqAddApuesta(Integer itemId, String usuarioNombre, Integer montoApuesta) {}
