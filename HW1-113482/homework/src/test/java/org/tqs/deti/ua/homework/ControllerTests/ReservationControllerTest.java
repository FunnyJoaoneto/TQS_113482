package org.tqs.deti.ua.homework.ControllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import org.tqs.deti.ua.homework.controller.ReservationController;
import org.tqs.deti.ua.homework.entities.Meal;
import org.tqs.deti.ua.homework.entities.Reservation;
import org.tqs.deti.ua.homework.service.MealService;
import org.tqs.deti.ua.homework.service.ReservationService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private ReservationService reservationService;

    @SuppressWarnings("removal")
    @MockBean
    private MealService mealService;

    @Test
    void testCreateReservation_Success() throws Exception {
        Meal meal = new Meal("Pasta", 12.5, "Yummy", 1);
        Reservation reservation = new Reservation("RES1234", 1, LocalDateTime.now().plusDays(1), 1);

        when(mealService.getMealById(1L)).thenReturn(meal);
        when(reservationService.createReservation(any(), any(), eq(1))).thenReturn(reservation);

        mockMvc.perform(post("/reservations")
                .param("mealIds", "1")
                .param("dateTime", LocalDateTime.now().plusDays(1).toString())
                .param("people", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("RES1234"));
    }

    @Test
    void testCreateReservation_Failure() throws Exception {
        Meal meal = new Meal("Pizza", 10.0, "Cheesy", 1);
        when(mealService.getMealById(1L)).thenReturn(meal);
        when(reservationService.createReservation(any(), any(), eq(1))).thenReturn(null);

        mockMvc.perform(post("/reservations")
                .param("mealIds", "1")
                .param("dateTime", LocalDateTime.now().plusDays(1).toString())
                .param("people", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid reservation: exceeds capacity or mismatch in people vs meals."));
    }

    @Test
    void testGetReservationByCode() throws Exception {
        Reservation reservation = new Reservation("RES1234", 1, LocalDateTime.now().plusDays(1), 2);
        when(reservationService.getReservationByCode("RES1234")).thenReturn(reservation);

        mockMvc.perform(get("/reservations/RES1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("RES1234"));
    }

    @Test
    void testGetReservationByCode_NotFound() throws Exception {
        when(reservationService.getReservationByCode("INVALID")).thenThrow(new IllegalArgumentException());

        mockMvc.perform(get("/reservations/INVALID"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllReservations() throws Exception {
        Reservation res = new Reservation("RES999", 1, LocalDateTime.now().plusDays(2), 2);
        when(reservationService.getAllReservations()).thenReturn(List.of(res));

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("RES999"));
    }

    @Test
    void testCheckIn_Success() throws Exception {
        when(reservationService.checkInReservation("RES1234")).thenReturn(true);

        mockMvc.perform(put("/reservations/RES1234/check-in"))
                .andExpect(status().isOk())
                .andExpect(content().string("Check-in successful"));
    }

    @Test
    void testCheckIn_Failure() throws Exception {
        when(reservationService.checkInReservation("RES1234")).thenReturn(false);

        mockMvc.perform(put("/reservations/RES1234/check-in"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid or already used reservation"));
    }

    @Test
    void testUpdateReservation_Success() throws Exception {
        Meal meal = new Meal("Pasta", 12.5, "Tasty", 1);
        Reservation updated = new Reservation("RES1234", 1, LocalDateTime.now().plusDays(3), 1);

        when(mealService.getMealById(1L)).thenReturn(meal);
        when(reservationService.updateReservation(eq("RES1234"), any(), any(), eq(1))).thenReturn(updated);

        mockMvc.perform(put("/reservations/RES1234/update")
                .param("mealIds", "1")
                .param("dateTime", LocalDateTime.now().plusDays(3).toString())
                .param("people", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("RES1234"));
    }

    @Test
    void testDeleteReservation_Success() throws Exception {
        when(reservationService.deleteByCode("RES1234")).thenReturn(true);

        mockMvc.perform(delete("/reservations/RES1234"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reservation deleted"));
    }

    @Test
    void testDeleteReservation_Failure() throws Exception {
        when(reservationService.deleteByCode("RES1234")).thenReturn(false);

        mockMvc.perform(delete("/reservations/RES1234"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid reservation code"));
    }

    @Test
    void testGetReservationsByRestaurant() throws Exception {
        Reservation res = new Reservation("RES5678", 1, LocalDateTime.now(), 3);
        when(reservationService.getReservationsByRestaurant(1L)).thenReturn(List.of(res));

        mockMvc.perform(get("/reservations/restaurant/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("RES5678"));
    }

    @Test
    void testGetReservationsByRestaurant_InvalidId() throws Exception {
        when(reservationService.getReservationsByRestaurant(99L)).thenThrow(new IllegalArgumentException());

        mockMvc.perform(get("/reservations/restaurant/99"))
                .andExpect(status().isBadRequest());
    }
}
