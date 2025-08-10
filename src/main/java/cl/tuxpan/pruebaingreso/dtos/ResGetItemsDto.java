package cl.tuxpan.pruebaingreso.dtos;

import java.util.List;

/**
 * Response wrapper for listing items.
 *
 * @param items List of item representations
 */

public record ResGetItemsDto(List<ResItemDto> items) {}
