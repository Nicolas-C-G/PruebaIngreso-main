package cl.tuxpan.pruebaingreso.services;

import cl.tuxpan.pruebaingreso.dtos.*;
import cl.tuxpan.pruebaingreso.models.ItemModel;
import cl.tuxpan.pruebaingreso.models.ApuestaModel;
import cl.tuxpan.pruebaingreso.models.UsuarioModel;
import cl.tuxpan.pruebaingreso.repositories.ItemRepository;
import cl.tuxpan.pruebaingreso.repositories.ApuestaRepository;
import cl.tuxpan.pruebaingreso.repositories.UsuarioRepository;
import cl.tuxpan.pruebaingreso.services.interfaces.FrontService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.math.BigDecimal;


/**
 * Default implementation of {@link cl.tuxpan.pruebaingreso.services.interfaces.FrontService}.
 * <p>
 * Encapsulates application logic for creating items, placing bets, and
 * determining winners. Uses Spring Data JPA repositories for persistence.
 */
@Slf4j
@Service
/** {@inheritDoc} */
public class FrontServiceImpl implements FrontService {

  ItemRepository itemRepository;
  ApuestaRepository apuestaRepository;
  UsuarioRepository usuarioRepository;

  FrontServiceImpl(
      ItemRepository itemRepository,
      ApuestaRepository apuestaRepository,
      UsuarioRepository usuarioRepository) {
    this.itemRepository = itemRepository;
    this.apuestaRepository = apuestaRepository;
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  /** {@inheritDoc} */
  public ResItemDto addItem(ReqAddItemDto reqAddItemDto) {
    log.warn("addItem: {}", reqAddItemDto);
    ItemModel item =
        itemRepository.save(new ItemModel(null, reqAddItemDto.name(), Collections.emptyList()));
    return new ResItemDto(item.getId(), item.getName(), Collections.emptyList());
  }

  @Override
  /** {@inheritDoc} */
  public ResGetItemsDto getItems() {
    return new ResGetItemsDto(
        itemRepository.findAll().stream()
            .map(
                itemModel ->
                    new ResItemDto(
                        itemModel.getId(),
                        itemModel.getName(),
                        itemModel.getApuestas().stream().map(ApuestaModel::getId).toList()))
            .toList());
  }

  @Override
  /** {@inheritDoc} */
  public ResItemDto getItem(Integer id) {
    return itemRepository
        .findById(id)
        .map(
            itemModel ->
                new ResItemDto(
                    itemModel.getId(),
                    itemModel.getName(),
                    itemModel.getApuestas().stream().map(ApuestaModel::getId).toList()))
        .orElse(null);
  }

  @Override
  /** {@inheritDoc} */
  public ResApuestaDto addApuesta(ReqAddApuestaDto reqAddApuestaDto) {
    Optional<ItemModel> itemModel = itemRepository.findById(reqAddApuestaDto.itemId());
    ItemModel item = itemModel.orElseThrow();

    Optional<UsuarioModel> usuarioModel =
        usuarioRepository.findByName(reqAddApuestaDto.usuarioNombre());

    UsuarioModel usuario =
        usuarioModel.orElseGet(
            () ->
                usuarioRepository.save(
                    new UsuarioModel(null, reqAddApuestaDto.usuarioNombre(), new ArrayList<>())));

    ApuestaModel apuesta =
        apuestaRepository.save(new ApuestaModel(null, reqAddApuestaDto.montoApuesta(), usuario, item));

    return new ResApuestaDto(apuesta.getId(), apuesta.getAmount());
  }

  @Override
  /** {@inheritDoc} */
  public ResWinnerDto getWinner(Integer id) {
    Optional<ApuestaModel> apuestaModel = apuestaRepository.findMaxBid(id);
    return apuestaModel
        .map(
            apuesta ->
                new ResWinnerDto(
                    apuesta.getItem().getId(),
                    apuesta.getItem().getName(),
                    apuesta.getUsuario().getId(),
                    apuesta.getUsuario().getName(),
                    apuesta.getAmount()))
        .orElse(null);
  }

  @Override
  /**
   * {@inheritDoc}
   */
  public ResTotalBetUserDto getUserTotalBet(Integer userId) {
    //BigDecimal total = apuestaRepository.sumAmountByUserId(userId);
    BigDecimal total = Optional.ofNullable(apuestaRepository.sumAmountByUserId(userId))
            .orElse(BigDecimal.ZERO);
    return new ResTotalBetUserDto(userId, total);
  }

}
