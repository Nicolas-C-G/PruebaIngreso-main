package cl.tuxpan.pruebaingreso.dtos;

/** Total amount bet by a specific user
 * added 2025-08-10 by Nicolas
 * @param userId User identifier.
 * @param total  Total bet amount.
 * */
public record ResTotalDto(Integer userId, Integer total) { }
