package org.tqs.deti.ua.homework.TestContainers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
class RestaurantControllerIT {

        @Container
        static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
                        .withDatabaseName("testdb")
                        .withUsername("testuser")
                        .withPassword("testpass");

        @DynamicPropertySource
        static void overrideProps(DynamicPropertyRegistry registry) {
                registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
                registry.add("spring.datasource.username", postgresContainer::getUsername);
                registry.add("spring.datasource.password", postgresContainer::getPassword);
                registry.add("spring.flyway.enabled", () -> "true");
        }

        @Autowired
        private MockMvc mockMvc;

        @Test
        @Order(1)
        void testGetAllRestaurants() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.get("/restaurants"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", not(empty())))
                                .andExpect(jsonPath("$[0].name").value("Campus Cafeteria"))
                                .andDo(print());
        }

        @Test
        @Order(2)
        void testCreateRestaurant() throws Exception {
                String json = """
                                {
                                  "name": "Testaurant",
                                  "location": "Porto",
                                  "locationCode": 1010500,
                                  "capacity": 40,
                                  "openingTime": "12:00",
                                  "closingTime": "22:00"
                                }
                                """;

                mockMvc.perform(MockMvcRequestBuilders.post("/restaurants")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Testaurant"))
                                .andExpect(jsonPath("$.capacity").value(40))
                                .andDo(print());
        }

        @Test
        @Order(3)
        void testUpdateRestaurant_Success() throws Exception {
                String updateJson = """
                                {
                                  "name": "UpdatedName",
                                  "location": "Lisbon",
                                  "locationCode": 1010500,
                                  "capacity": 60,
                                  "openingTime": "10:00",
                                  "closingTime": "20:00"
                                }
                                """;

                mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateJson))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("UpdatedName"))
                                .andDo(print());
        }

        @Test
        @Order(4)
        void testUpdateRestaurant_NotFound() throws Exception {
                String updateJson = """
                                {
                                  "name": "NonExistent",
                                  "location": "Nowhere",
                                  "locationCode": 999999,
                                  "capacity": 0,
                                  "openingTime": "00:00",
                                  "closingTime": "00:00"
                                }
                                """;

                mockMvc.perform(MockMvcRequestBuilders.put("/restaurants/999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updateJson))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string(containsString("Restaurant with id 999 does not exist.")))
                                .andDo(print());
        }

        @Test
        @Order(5)
        void testDeleteRestaurant_Success() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/1"))
                                .andExpect(status().isOk());
        }

        @Test
        @Order(6)
        void testDeleteRestaurant_NotFound() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.delete("/restaurants/999"))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string(containsString("Restaurant with id 999 does not exist.")));
        }

        @Test
        @Order(7)
        void testGetRestaurantById_Success() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/2"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Library Cafe"));
        }

        @Test
        @Order(8)
        void testGetRestaurantById_NotFound() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.get("/restaurants/999"))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string(containsString("Restaurant with id 999 does not exist.")));
        }
}
