package org.tqs.deti.ua.homework.TestContainers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReservationControllerIT {

        static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2")
                        .withDatabaseName("testdb")
                        .withUsername("test")
                        .withPassword("test");

        @DynamicPropertySource
        static void properties(DynamicPropertyRegistry registry) {
                postgres.start();
                registry.add("spring.datasource.url", postgres::getJdbcUrl);
                registry.add("spring.datasource.username", postgres::getUsername);
                registry.add("spring.datasource.password", postgres::getPassword);
        }

        @Autowired
        private MockMvc mockMvc;

        private static final Long EXISTING_MEAL_ID = 1L;
        private static final Long EXISTING_RESTAURANT_ID = 1L;
        private static String createdReservationCode;

        @Test
        @Order(1)
        void testCreateReservation_Success() throws Exception {
                String dateTime = LocalDateTime.now().plusDays(1)
                                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                var result = mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                                .param("mealIds", EXISTING_MEAL_ID.toString(), EXISTING_MEAL_ID.toString())
                                .param("dateTime", dateTime)
                                .param("people", "2"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.code").exists())
                                .andReturn();

                String content = result.getResponse().getContentAsString();
                createdReservationCode = content.split("\"code\":\"")[1].split("\"")[0];
        }

        @Test
        @Order(2)
        void testGetReservationByCode_Success() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.get("/reservations/" + createdReservationCode))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.code").value(createdReservationCode));
        }

        @Test
        @Order(3)
        void testCheckIn_Success() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.put("/reservations/" + createdReservationCode + "/check-in"))
                                .andExpect(status().isOk())
                                .andExpect(content().string(containsString("successful")));
        }

        @Test
        @Order(4)
        void testUpdateReservation_Success() throws Exception {
                String newDateTime = LocalDateTime.now().plusDays(2)
                                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                mockMvc.perform(MockMvcRequestBuilders.put("/reservations/" + createdReservationCode + "/update")
                                .param("mealIds", EXISTING_MEAL_ID.toString())
                                .param("dateTime", newDateTime)
                                .param("people", "3"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.people").value(3));
        }

        @Test
        @Order(5)
        void testGetAllReservations_Success() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.get("/reservations"))
                                .andExpect(status().isOk())
                                .andExpect(content().string(containsString(createdReservationCode)));
        }

        @Test
        @Order(6)
        void testGetByRestaurant_Success() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.get("/reservations/restaurant/" + EXISTING_RESTAURANT_ID))
                                .andExpect(status().isOk())
                                .andExpect(content().string(containsString(createdReservationCode)));
        }

        @Test
        @Order(7)
        void testDeleteReservation_Success() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.delete("/reservations/" + createdReservationCode))
                                .andExpect(status().isOk())
                                .andExpect(content().string(containsString("deleted")));
        }

        @Test
        @Order(8)
        void testGetDeletedReservation_Fails() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.get("/reservations/" + createdReservationCode))
                                .andExpect(status().isBadRequest());
        }
}
