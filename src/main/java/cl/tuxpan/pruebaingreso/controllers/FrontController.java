package cl.tuxpan.pruebaingreso.controllers;

import cl.tuxpan.pruebaingreso.dtos.*;
import cl.tuxpan.pruebaingreso.repositories.UsuarioRepository;
import cl.tuxpan.pruebaingreso.services.interfaces.FrontService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.micrometer.core.annotation.Timed;


/**
 * Public REST API controller for managing auction items and bets.
 * <p>
 * Base path: {@code /api/v1}.
 * Provides endpoints to create and list items, place bets, query the current winner,
 * and retrieve user betting information.
 */
@RestController
@RequestMapping("/api/v1")
public class FrontController {

  private final UsuarioRepository usuarioRepository;
  private final FrontService frontService;

  /**
   * Constructs a {@code FrontController} with the required services and repositories.
   *
   * @param frontService       the service handling auction and bet operations
   * @param usuarioRepository  the repository for accessing user data
   */
  @Autowired
  FrontController(FrontService frontService, UsuarioRepository usuarioRepository) {
    this.frontService = frontService;
    this.usuarioRepository = usuarioRepository;
  }

  /**
   * Creates a new auction item.
   *
   * @param reqAddItemDto: Request payload containing the item name.
   * @return Created item representation with its generated identifier.
   * */

  @PostMapping("/item")
  public ResItemDto addItem(@RequestBody ReqAddItemDto reqAddItemDto) {
    return frontService.addItem(reqAddItemDto);
  }

  /**
   * Returns an item by its identifier.
   *
   * @param id: Item identifier (path variable)
   * @return Item representation, or an error if not found
   * */

  @GetMapping("/item/{id}")
  public ResItemDto getItem(@PathVariable Integer id) {
    return frontService.getItem(id);
  }

  /**
   * Lists all available items.
   *
   * @return A wrapper dto containing the list of item (Data transfer object)
   * */

  @GetMapping("/item")
  public ResGetItemsDto getItems() {
    return frontService.getItems();
  }

  /**
   * Places a new bet on an existing item.
   * <p>
   * If the user does not exist, the service may create it automatically.
   *
   * @param reqAddApuestaDto: Request payload with item id, user name and bet amount.
   * @return The persisted bet representation
   * */

  @PostMapping("/apuesta")
  public ResApuestaDto addApuesta(@RequestBody @jakarta.validation.Valid ReqAddApuestaDto reqAddApuestaDto) {
    return frontService.addApuesta(reqAddApuestaDto);
  }

  /**
   * Returns the current winner, highest bid, for a given item.
   *
   * @param itemId: Item identifier (path variable)
   * @return Winner data (item + user + amount)  or {@code null} if the item has no bets
   * */
  @Timed(value = "winner.endpoint", percentiles = {0.5, 0.9, 0.99})
  @GetMapping("/winner/{itemId}")
  public ResWinnerDto getWinner(@PathVariable Integer itemId) {
    return frontService.getWinner(itemId);
  }

  /**
   * Returns the total bet amount placed by a specific user.
   *
   * @param userId: User identifier (path variable)
   * @return A DTO with the user id and the aggregated total bet amount
   * */
  @GetMapping("/users/{userId}/bets/total")
  public ResTotalBetUserDto getUserTotalBet(@PathVariable Integer userId) {
    return frontService.getUserTotalBet(userId);
  }

  /**
   * Retrieves all bets placed by a specific user.
   *
   * @param userId user identifier (path variable)
   * @return a list of detailed bet representations for the given user
   */

  @GetMapping("/users/{userId}/apuestas")
  public java.util.List<ResApuestaDetailDto> getUserBets(@PathVariable Integer userId) {
    return frontService.getUserBets(userId);
  }

  /**
   * Retrieves a user by its identifier.
   * <p>
   * Throws a {@link RuntimeException} if the user is not found.
   *
   * @param userId user identifier (path variable)
   * @return a {@link ResUsuarioDto} containing the user's ID and name
   */

  @GetMapping("/users/{userId}")
  public ResUsuarioDto getUser(@PathVariable Integer userId) {
    var user = usuarioRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    return new ResUsuarioDto(user.getId(), user.getName());
  }
}
