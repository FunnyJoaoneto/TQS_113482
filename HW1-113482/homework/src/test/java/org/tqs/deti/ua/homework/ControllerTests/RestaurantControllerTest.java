package org.tqs.deti.ua.homework.ControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.tqs.deti.ua.homework.controller.RestaurantController;
import org.tqs.deti.ua.homework.entities.Restaurant;
import org.tqs.deti.ua.homework.service.RestaurantService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @SuppressWarnings("removal")
        @MockBean
        private RestaurantService restaurantService;

        @Test
        void testGetRestaurants() throws Exception {
                Restaurant r = new Restaurant("Sushi House", "Lisbon", 1010500, 50, "10:00", "23:00");
                r.setId(1);

                when(restaurantService.getAllRestaurants())
                                .thenReturn(List.of(r));

                mockMvc.perform(get("/restaurants"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].name").value("Sushi House"));
        }

        @Test
        void testCreateRestaurant() throws Exception {
                Restaurant r = new Restaurant("Grill Point", "Porto", 1131200, 80, "12:00", "22:00");
                r.setId(1);

                when(restaurantService.createRestaurant(any())).thenReturn(r);

                mockMvc.perform(post("/restaurants")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(r)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Grill Point"));
        }

        @Test
        void testDeleteRestaurant() throws Exception {
                mockMvc.perform(delete("/restaurants/1"))
                                .andExpect(status().isOk());

                verify(restaurantService).deleteRestaurant(1L);
        }

        @Test
        void testGetRestaurant() throws Exception {
                Restaurant r = new Restaurant("Test", "Braga", 1171400, 40, "11:00", "21:00");
                r.setId(1);

                when(restaurantService.getRestaurantById(1L)).thenReturn(r);

                mockMvc.perform(get("/restaurants/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Test"));
        }

        @Test
        void testUpdateRestaurantSuccess() throws Exception {
                Restaurant existingRestaurant = new Restaurant("Sushi House", "Lisbon", 1010500, 50, "10:00", "23:00");
                existingRestaurant.setId(1);

                Restaurant updatedRestaurant = new Restaurant("Sushi House Deluxe", "Lisbon", 1010500, 60, "10:00",
                                "23:30");
                updatedRestaurant.setId(1);

                // When the service is called to update the restaurant, it should return the
                // updated restaurant
                when(restaurantService.updateRestaurant(eq(1L), any())).thenReturn(updatedRestaurant);

                mockMvc.perform(put("/restaurants/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(updatedRestaurant)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Sushi House Deluxe"))
                                .andExpect(jsonPath("$.capacity").value(60))
                                .andExpect(jsonPath("$.openingTime").value("10:00"))
                                .andExpect(jsonPath("$.closingTime").value("23:30"));
        }

        @Test
        void testGetRestaurantNotFound() throws Exception {
                when(restaurantService.getRestaurantById(99L))
                                .thenThrow(new IllegalArgumentException("Restaurant with id 99 does not exist."));

                mockMvc.perform(get("/restaurants/99"))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string("Restaurant with id 99 does not exist."));
        }

        @Test
        void testDeleteRestaurantNotFound() throws Exception {
                doThrow(new IllegalArgumentException("Restaurant with id 99 does not exist."))
                                .when(restaurantService).deleteRestaurant(99L);

                mockMvc.perform(delete("/restaurants/99"))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string("Restaurant with id 99 does not exist."));
        }

        @Test
        void testUpdateRestaurantNotFound() throws Exception {
                Restaurant r = new Restaurant("Fake", "Nowhere", 1234567, 20, "00:00", "00:00");

                when(restaurantService.updateRestaurant(eq(99L), any()))
                                .thenThrow(new IllegalArgumentException("Restaurant with id 99 does not exist."));

                mockMvc.perform(put("/restaurants/99")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(r)))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string("Restaurant with id 99 does not exist."));
        }

}
