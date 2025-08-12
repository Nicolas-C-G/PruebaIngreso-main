package cl.tuxpan.pruebaingreso.dtos;

/**
 * Data transfer object representing a user in the system.
 * <p>
 * This DTO is typically returned when retrieving user information
 * via the API, containing only the essential identification and
 * display name fields.
 *
 * @param id      the unique identifier of the user
 * @param nombre  the display name of the user
 */
public record ResUsuarioDto(Integer id, String nombre) {}
