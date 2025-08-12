package cl.tuxpan.pruebaingreso.services.interfaces;

import cl.tuxpan.pruebaingreso.dtos.*;

import java.util.List;

/**
 * Application service boundary for the auction use cases.
 *
 * Coordinates repository access and domain rules for items and bets.
 * */

public interface FrontService {

  /**
   * Creates a new auction item.
   *
   * @param reqAddItemDto: input with the item name.
   * @return Created item representation.
   * */

  ResItemDto addItem(ReqAddItemDto reqAddItemDto);

  /**
   * Lists all items.
   *
   * @return Return a wrapper DTO containing the list of items.
   * */

  ResGetItemsDto getItems();

  /**
   * Retrieves a single item by its identifier.
   *
   * @param id: Item identifier
   * @return Item representation, or an error if not found
   * */

  ResItemDto getItem(Integer id);

  /**
   * Places a new bet.
   *
   * @param reqAddApuestaDto: Input with item id, user name and amount.
   * @return Bet representation.
   * */

  ResApuestaDto addApuesta(ReqAddApuestaDto reqAddApuestaDto);

  /**
   * Computes the current winner, highest bet, for an item.
   *
   * @param id: Item identifier.
   * @return Return the Winner or {@code null} if no existing bets.
   * */
  ResWinnerDto getWinner(Integer id);

  /**
   * Retrieves the total bet amount placed by a given user.
   *
   * @param userId the identifier of the user
   * @return a {@link ResTotalBetUserDto} containing the user's ID and total bet amount
   */
  ResTotalBetUserDto getUserTotalBet(Integer userId);

  /**
   * Retrieves all bets placed by a given user.
   *
   * @param userId the identifier of the user
   * @return a list of {@link ResApuestaDetailDto} representing the user's bets
   */
  List<ResApuestaDetailDto> getUserBets(Integer userId);
}
