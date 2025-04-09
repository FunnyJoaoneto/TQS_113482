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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MealControllerIT {

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

    private static final Long EXISTING_RESTAURANT_ID = 1L;
    private static Long createdMealId;

    @Test
    @Order(1)
    void testCreateMeal_Success() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/meals")
                .param("restaurantId", EXISTING_RESTAURANT_ID.toString())
                .param("name", "Test Meal")
                .param("price", "12.5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Meal"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        createdMealId = Long.parseLong(content.split("\"id\":")[1].split(",")[0]); // Extract meal ID
    }

    @Test
    @Order(2)
    void testGetMealsForRestaurant_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/meals")
                .param("restaurantId", EXISTING_RESTAURANT_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test Meal")));
    }

    @Test
    @Order(3)
    void testGetMealById_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/meals/" + createdMealId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Meal"));
    }

    @Test
    @Order(4)
    void testUpdateMeal_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/meals/update/" + createdMealId)
                .param("name", "Updated Meal")
                .param("price", "15.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Meal"));
    }

    @Test
    @Order(5)
    void testDeleteMeal_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/meals/delete/" + createdMealId))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void testGetMealById_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/meals/" + createdMealId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("not")));
    }
}
