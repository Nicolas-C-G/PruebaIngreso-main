package cl.tuxpan.pruebaingreso.dtos;

import java.math.BigDecimal;

/**
 * DTO for representing the total bet amount of a specific user.
 *
 * @param userId: The unique identifier of the user
 * @param total: The total amount of bets placed by the user
 */
public record ResTotalBetUserDto(Integer userId, BigDecimal total) {}
