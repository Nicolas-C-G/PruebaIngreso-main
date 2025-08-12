package cl.tuxpan.pruebaingreso.controllers;

import cl.tuxpan.pruebaingreso.repositories.ApuestaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Administrative controller providing endpoints for managing and inspecting bets.
 * <p>
 * This controller is primarily intended for development and testing purposes.
 * If deploying to a production environment, uncomment the {@code @Profile} annotation
 * to restrict exposure to specific application profiles (e.g., {@code dev}, {@code test}).
 * <p>
 * All endpoints are prefixed with {@code /api/v1/admin}.
 */
//Comment profile just for testing purposes
//@Profile({"dev","test"}) // expose only in dev/test
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ApuestaRepository apuestaRepository;

    /**
     * Immutable projection for representing bet data in administrative views.
     *
     * @param id        the unique identifier of the bet
     * @param amount    the wagered amount
     * @param itemId    the unique identifier of the item associated with the bet
     * @param userName  the name of the user who placed the bet
     */
    public record BetView(Integer id, Integer amount, Integer itemId, String userName) {}

    /**
     * Retrieves all bets from the repository and returns them as {@link BetView} objects.
     * <p>
     * This method fetches all {@code Apuesta} entities from the database,
     * transforms each into a {@code BetView} containing the bet ID, amount,
     * associated item ID, and the bettor's name, and then returns the list.
     *
     * @return a list of {@link BetView} representing all bets in the system
     */
    @GetMapping("/apuestas")
    public List<BetView> listBets() {
        return apuestaRepository.findAll().stream()
                .map(a -> new BetView(
                        a.getId(),
                        a.getAmount(),
                        a.getItem().getId(),
                        a.getUsuario().getName()
                ))
                .toList();
    }
}
