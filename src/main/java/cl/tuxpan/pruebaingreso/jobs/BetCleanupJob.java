package cl.tuxpan.pruebaingreso.jobs;

import cl.tuxpan.pruebaingreso.repositories.ApuestaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BetCleanupJob {

    private final ApuestaRepository apuestaRepository;

    @Scheduled(cron = "0 */2 * * * *") // every hour
    @Transactional
    public void clean() {
        int deleted = apuestaRepository.deleteBetsWithSpecialCharUsernames();
        log.info("[BetCleanupJob] Deleted {} bets with special-char usernames", deleted);
    }
}
