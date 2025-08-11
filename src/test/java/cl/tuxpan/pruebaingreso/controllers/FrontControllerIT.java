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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}
