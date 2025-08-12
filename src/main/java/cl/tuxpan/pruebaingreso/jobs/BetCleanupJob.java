package cl.tuxpan.pruebaingreso.jobs;

import cl.tuxpan.pruebaingreso.repositories.ApuestaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Scheduled job responsible for cleaning up invalid bets.
 * <p>
 * This job removes all bets placed by users whose usernames contain
 * special characters (as defined by the repository's query logic).
 * <p>
 * It is scheduled to run periodically according to the configured
 * cron expression and logs the number of bets deleted.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BetCleanupJob {

    private final ApuestaRepository apuestaRepository;

    /**
     * Executes the cleanup process for bets with usernames containing special characters every 2 minutes.
     * */
    @Scheduled(cron = "0 */2 * * * *") // every 2 minutes
    @Transactional
    public void clean() {
        int deleted = apuestaRepository.deleteBetsWithSpecialCharUsernames();
        log.info("[BetCleanupJob] Deleted {} bets with special-char usernames", deleted);
    }
}
