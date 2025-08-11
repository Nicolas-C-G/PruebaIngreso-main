package cl.tuxpan.pruebaingreso.controllers;

import cl.tuxpan.pruebaingreso.repositories.ApuestaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Comment profile just for testing purposes
//@Profile({"dev","test"}) // expose only in dev/test
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ApuestaRepository apuestaRepository;

    /**
     * Lightweight view for listing bets via admin endpoint.
     */
    public record BetView(Integer id, Integer amount, Integer itemId, String userName) {}

    /**
     * Lists all bets (for debugging/validation).
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
