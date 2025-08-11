package cl.tuxpan.pruebaingreso.controllers;

import cl.tuxpan.pruebaingreso.dtos.*;
import cl.tuxpan.pruebaingreso.services.interfaces.FrontService;
import org.springframework.web.bind.annotation.*;


/**
 * Public Rest Api
 * Base path: {@code /api/v1}. Exposes endpoints to create/list items, place bets and query the current winner for an item
 * */
@RestController
@RequestMapping("/api/v1")
public class FrontController {

  FrontService frontService;

  FrontController(FrontService frontService) {
    this.frontService = frontService;
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
   *
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

}
