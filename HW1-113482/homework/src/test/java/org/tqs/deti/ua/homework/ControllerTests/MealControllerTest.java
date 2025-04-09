package org.tqs.deti.ua.homework.ControllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.tqs.deti.ua.homework.controller.MealController;
import org.tqs.deti.ua.homework.entities.Meal;
import org.tqs.deti.ua.homework.service.MealService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MealController.class)
class MealControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @SuppressWarnings("removal")
        @MockBean
        private MealService mealService;

        @Test
        void testGetMealsByRestaurantId() throws Exception {
                Meal m = new Meal("Pizza", 12.5, "Delicious cheesy pizza", 1);
                m.setRestaurantId(1);
                when(mealService.getMealsForRestaurant(1L)).thenReturn(List.of(m));

                mockMvc.perform(get("/meals?restaurantId=1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].name").value("Pizza"))
                                .andExpect(jsonPath("$[0].description").value("Delicious cheesy pizza"));
        }

        @Test
        void testGetMealById() throws Exception {
                Meal m = new Meal("Burger", 8.0, "Juicy beef burger", 2);
                m.setRestaurantId(2);
                m.setPrice(8.0);
                when(mealService.getMealById(1L)).thenReturn(m);

                mockMvc.perform(get("/meals/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Burger"))
                                .andExpect(jsonPath("$.description").value("Juicy beef burger"));
        }

        @Test
        void testGetMealByIdNotFound() throws Exception {
                when(mealService.getMealById(99L)).thenThrow(new RuntimeException("Meal not found"));

                mockMvc.perform(get("/meals/99"))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string("Meal not found"));
        }

        @Test
        void testCreateMeal() throws Exception {
                Meal m = new Meal("Salad", 6.0, "Fresh green salad", 1);
                m.setRestaurantId(1);
                when(mealService.createMeal(1L, "Salad", 6.0)).thenReturn(m);

                mockMvc.perform(post("/meals")
                                .param("restaurantId", "1")
                                .param("name", "Salad")
                                .param("price", "6.0"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Salad"))
                                .andExpect(jsonPath("$.description").value("Fresh green salad"));
        }

        @Test
        void testCreateMealRestaurantNotFound() throws Exception {
                when(mealService.createMeal(99L, "Soup", 5.0))
                                .thenThrow(new IllegalArgumentException("Restaurant with id 99 does not exist."));

                mockMvc.perform(post("/meals")
                                .param("restaurantId", "99")
                                .param("name", "Soup")
                                .param("price", "5.0"))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string("Restaurant with id 99 does not exist."));
        }

        @Test
        void testUpdateMeal() throws Exception {
                Meal m = new Meal("Updated Meal", 10.0, "Updated description", 1);
                m.setRestaurantId(1);
                when(mealService.updateMeal(1L, "Updated Meal", 10.0)).thenReturn(m);

                mockMvc.perform(put("/meals/update/1")
                                .param("name", "Updated Meal")
                                .param("price", "10.0"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Updated Meal"))
                                .andExpect(jsonPath("$.description").value("Updated description"));
        }

        @Test
        void testUpdateMealNotFound() throws Exception {
                when(mealService.updateMeal(99L, "X", 1.0))
                                .thenThrow(new IllegalArgumentException("Meal with id 99 does not exist."));

                mockMvc.perform(put("/meals/update/99")
                                .param("name", "X")
                                .param("price", "1.0"))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string("Meal with id 99 does not exist."));
        }

        @Test
        void testDeleteMeal() throws Exception {
                mockMvc.perform(delete("/meals/delete/1"))
                                .andExpect(status().isOk());

                verify(mealService).deleteMeal(1L);
        }

        @Test
        void testDeleteMealNotFound() throws Exception {
                doThrow(new IllegalArgumentException("Meal with id 99 does not exist."))
                                .when(mealService).deleteMeal(99L);

                mockMvc.perform(delete("/meals/delete/99"))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string("Meal with id 99 does not exist."));
        }
}
