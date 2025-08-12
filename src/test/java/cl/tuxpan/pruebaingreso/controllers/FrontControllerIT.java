package cl.tuxpan.pruebaingreso.controllers;

import cl.tuxpan.pruebaingreso.dtos.ResTotalBetUserDto;
import cl.tuxpan.pruebaingreso.models.ApuestaModel;
import cl.tuxpan.pruebaingreso.models.ItemModel;
import cl.tuxpan.pruebaingreso.models.UsuarioModel;
import cl.tuxpan.pruebaingreso.repositories.ApuestaRepository;
import cl.tuxpan.pruebaingreso.repositories.ItemRepository;
import cl.tuxpan.pruebaingreso.repositories.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for FrontController's endpoint:
 * GET /api/v1/users/{userId}/bets/total
 */
@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.test.context.ActiveProfiles("test")
class FrontControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private ItemRepository itemRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ApuestaRepository apuestaRepository;

    @AfterEach
    void cleanup() {
        apuestaRepository.deleteAll();
        itemRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    /**
     * This test consists on the following steps:
     * 1.- Creates a user, alice, with no bets.
     * 2.- Calls Get /api/v1/users/{userId}/bets/total.
     * 3.- Expects 200 Ok and deserialize the JSON body into ResTotalBetUserDto.
     * 4.- Asserts userId matches and total == 0.
     * This step also confirms if the service is converting a null DB sum into BigDecimal.Zero
     * */
    @Test
    @DisplayName("Returns 0 when user has no bets")
    void total_is_zero_when_user_has_no_bets() throws Exception {
        // Given: a user without bets
        UsuarioModel user = usuarioRepository.save(new UsuarioModel(null, "alice", java.util.List.of()));

        // When
        String json = mockMvc.perform(get("/api/v1/users/{userId}/bets/total", user.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Then
        ResTotalBetUserDto body = objectMapper.readValue(json, ResTotalBetUserDto.class);
        assertThat(body.userId()).isEqualTo(user.getId());
        assertThat(body.total()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    /**
     * This test consists on the following steps:
     * 1.- Seeds one Item, two users, which are bob and carol.
     * 2.- Saves bets: for bob -> 10 + 25; for carol -> 99
     * 3.- Calls the endpoint for bob's userId.
     * 4.- Expects 200 OK, then asserts the response has userId == bob and total == 35.
     * This step also proves it sums only the requested user's bets and ignores others.
     * */
    @Test
    @DisplayName("Sums only the bets for the requested user")
    void sums_bets_for_user_only() throws Exception {
        // Given
        ItemModel item = itemRepository.save(new ItemModel(null, "Laptop", java.util.List.of()));
        UsuarioModel userA = usuarioRepository.save(new UsuarioModel(null, "bob", java.util.List.of()));
        UsuarioModel userB = usuarioRepository.save(new UsuarioModel(null, "carol", java.util.List.of()));

        // Bets for userA: 10.50 + 25.00 = 35.50
        apuestaRepository.save(new ApuestaModel(null, 10, userA, item));
        apuestaRepository.save(new ApuestaModel(null, 25, userA, item));

        // Bet for userB (should NOT be included)
        apuestaRepository.save(new ApuestaModel(null, 99, userB, item));

        // When
        String json = mockMvc.perform(get("/api/v1/users/{userId}/bets/total", userA.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // Then
        ResTotalBetUserDto body = objectMapper.readValue(json, ResTotalBetUserDto.class);
        assertThat(body.userId()).isEqualTo(userA.getId());
        assertThat(body.total()).isEqualByComparingTo(new BigDecimal("35"));
    }

    @Test
    @DisplayName("Rejects short user name (username len < 5 )")
    void rejects_short_username() throws Exception {
        // First create an item so itemId=1 exists
        ItemModel item = itemRepository.save(new ItemModel(null, "Laptop", java.util.List.of()));

        String body = """
                {"itemId":%d, "usuarioNombre":"abcd", "montoApuesta":1000}
                """.formatted(item.getId());

        mockMvc.perform(post("/api/v1/apuesta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Rejects out of range amount (amount < 1000)")
    void rejects_out_of_range_amount() throws Exception {
        // First create an item so itemId=1 exists
        ItemModel item = itemRepository.save(new ItemModel(null, "Laptop", java.util.List.of()));

        String body = """
            {"itemId":%d,"usuarioNombre":"validName","montoApuesta":999}
        """.formatted(item.getId());

        mockMvc.perform(post("/api/v1/apuesta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Accepts minimal valid values (username len=5 and amount=1000)")
    void accepts_min_boundary_values() throws Exception {
        // Given: an item to bet on
        ItemModel item = itemRepository.save(new ItemModel(null, "Phone", java.util.List.of()));

        // Minimal valid username length = 5, minimum amount = 1000
        String username = "abcde"; // Just 5 characters
        int amount = 1000;

        String body = """
                {"itemId": %d, "usuarioNombre": "%s", "montoApuesta": %d}
                """.formatted(item.getId(), username, amount);

        mockMvc.perform(post("/api/v1/apuesta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        // Verify persisted data
        var all = apuestaRepository.findAll();
        assertThat(all).hasSize(1);
        ApuestaModel saved = all.get(0);
        assertThat(saved.getAmount()).isEqualTo(amount);
        assertThat(saved.getUsuario().getName()).isEqualTo(username);
        assertThat(saved.getItem().getId()).isEqualTo(item.getId());
    }

    @Test
    @DisplayName("Accepts maximal valid values (username len=50, amount=999,999,999)")
    void accepts_max_boundary_values() throws Exception {
        // Given
        ItemModel item = itemRepository.save(new ItemModel(null, "TV", java.util.List.of()));

        // Build a 50-char username
        String username50 = "u".repeat(50);
        int amount = 999_999_999;

        String body = """
        {"itemId":%d,"usuarioNombre":"%s","montoApuesta":%d}
    """.formatted(item.getId(), username50, amount);

        // When / Then
        mockMvc.perform(post("/api/v1/apuesta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        // Verify persisted data
        var all = apuestaRepository.findAll();
        assertThat(all).hasSize(1);
        ApuestaModel saved = all.get(0);
        assertThat(saved.getAmount()).isEqualTo(amount);
        assertThat(saved.getUsuario().getName()).isEqualTo(username50);
        assertThat(saved.getItem().getId()).isEqualTo(item.getId());
    }

    @Test
    @DisplayName("Measure latency of /winner/{itemId}")
    void measure_winner_endpoint_latency() throws Exception {
        // ----- Arrange: create realistic data set -----
        ItemModel item = itemRepository.save(new ItemModel(null, "PerfItem", java.util.List.of()));
        UsuarioModel user = usuarioRepository.save(new UsuarioModel(null, "perf_user", java.util.List.of()));

        // Seed N bets so findMaxBid has some work to do
        final int betCount = Integer.getInteger("perf.bets", 1_000);
        for (int i = 1; i <= betCount; i++) {
            // amounts must be integers in your model
            apuestaRepository.save(new ApuestaModel(null, i, user, item));
        }
        assertThat(apuestaRepository.count()).isEqualTo(betCount);

        final String url = "/api/v1/winner/" + item.getId();

        // ----- Warm-up -----
        final int warmup = Integer.getInteger("perf.warmup", 10);
        for (int i = 0; i < warmup; i++) {
            mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        // ----- Measure -----
        final int runs = Integer.getInteger("perf.runs", 50);
        long[] timesNs = new long[runs];

        for (int i = 0; i < runs; i++) {
            long t0 = System.nanoTime();
            mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
            long t1 = System.nanoTime();
            timesNs[i] = (t1 - t0);
        }

        // ----- Stats -----
        Arrays.sort(timesNs);
        double avgMs = Arrays.stream(timesNs).average().orElse(0) / 1_000_000.0;
        double p95Ms = timesNs[(int)Math.floor(runs * 0.95) - 1] / 1_000_000.0;
        double maxMs = timesNs[runs - 1] / 1_000_000.0;

        System.out.printf("Winner endpoint perf: runs=%d, bets=%d, avg=%.2f ms, p95=%.2f ms, max=%.2f ms%n",
                runs, betCount, avgMs, p95Ms, maxMs);

        // Optional: VERY loose assertion just to catch regressions
        // Assert avg under 500ms for this in-memory setup (tune as you like or remove)
        assertThat(avgMs).isLessThan(500.0);
    }
}
