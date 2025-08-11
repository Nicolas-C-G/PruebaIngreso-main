package cl.tuxpan.pruebaingreso.dtos;

import jakarta.validation.constraints.*;

/**
 * Request payload for placing a new bet (apuesta).
 *
 * @param itemId        Identifier of the item to bet on
 * @param usuarioNombre Name of the user placing the bet
 * @param montoApuesta  Amount of the bet
 */

public record ReqAddApuestaDto(
        @NotNull Integer itemId,
        @NotBlank
        @Size(min = 5, max = 50)
        String usuarioNombre,
        @NotNull
        @Min(1000)
        @Max(999999999)
        Integer montoApuesta
) {}
