package cl.tuxpan.pruebaingreso.services;

import java.math.BigDecimal;
import cl.tuxpan.pruebaingreso.repositories.ApuestaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service layer for handling business logic related to bets.
 */
@Service
@RequiredArgsConstructor
public class ApuestaService {

    private final ApuestaRepository apuestaRepository;

    /**
     * Retrieves the total bet amount placed by a specific user.
     *
     * @param userId the unique identifier of the user
     * @return the total bet amount for the given user, or {@code 0} if the user has no bets
     */
    public BigDecimal getUserTotalBet(Integer userId) {
        return apuestaRepository.sumAmountByUserId(userId);
    }
}