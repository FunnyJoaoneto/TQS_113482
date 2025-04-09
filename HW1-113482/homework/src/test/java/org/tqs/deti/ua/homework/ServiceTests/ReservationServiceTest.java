package org.tqs.deti.ua.homework.ServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.tqs.deti.ua.homework.entities.*;
import org.tqs.deti.ua.homework.repository.*;
import org.tqs.deti.ua.homework.service.ReservationService;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateReservation_Success() {
        Meal meal = new Meal("Pizza", 12.5, "Delicious cheesy pizza", 1);
        List<Meal> meals = List.of(meal);

        LocalDateTime dateTime = LocalDateTime.now();
        int people = 1;

        Restaurant restaurant = new Restaurant("Sushi House", "Lisbon", 1010500, 50, "10:00", "23:00");

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(reservationRepository.findByRestaurantIdAndDateTime(1L, dateTime)).thenReturn(List.of());
        when(reservationRepository.existsByCode(anyString())).thenReturn(false);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> i.getArgument(0));

        Reservation result = reservationService.createReservation(meals, dateTime, people);

        assertNotNull(result);
        assertEquals(1, result.getPeople());
        assertEquals(1, result.getMealReservations().size());
        verify(reservationRepository).save(result);
    }

    @Test
    void testCreateReservation_EmptyMeals_ReturnsNull() {
        Reservation result = reservationService.createReservation(List.of(), LocalDateTime.now(), 2);
        assertNull(result);
    }

    @Test
    void testCreateReservation_ExceedsCapacity_ReturnsNull() {
        Meal meal = new Meal("Burguer", 12.5, "Delicious cheesy pizza", 1);
        List<Meal> meals = List.of(meal);
        LocalDateTime dateTime = LocalDateTime.now();

        Restaurant restaurant = new Restaurant("Burger Town", "Porto", 1010500, 2, "11:00", "22:00");

        Reservation existing = new Reservation("CODE123", 1, dateTime, 2);

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(reservationRepository.findByRestaurantIdAndDateTime(1L, dateTime)).thenReturn(List.of(existing));

        Reservation result = reservationService.createReservation(meals, dateTime, 2);

        assertNull(result);
    }

    @Test
    void testCreateReservation_MealCountMismatch_ReturnsNull() {
        Meal meal = new Meal("Pizza", 12.5, "Delicious cheesy pizza", 1);
        List<Meal> meals = List.of(meal); // 1 meal
        LocalDateTime dateTime = LocalDateTime.now();

        Restaurant restaurant = new Restaurant("Pizza Place", "Faro", 1010500, 10, "09:00", "20:00");

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(reservationRepository.findByRestaurantIdAndDateTime(1L, dateTime)).thenReturn(List.of());
        when(reservationRepository.existsByCode(anyString())).thenReturn(false);

        Reservation result = reservationService.createReservation(meals, dateTime, 2); // 2 people

        assertNull(result);
    }

    @Test
    void testGetReservationByCode_Success() {
        String code = "CODE123";
        Reservation reservation = new Reservation(code, 1, LocalDateTime.now(), 2);

        when(reservationRepository.existsByCode(code)).thenReturn(true);
        when(reservationRepository.findByCode(code)).thenReturn(reservation);

        Reservation result = reservationService.getReservationByCode(code);

        assertNotNull(result);
        assertEquals(code, result.getCode());
    }

    @Test
    void testGetReservationByCode_NotFound_ThrowsException() {
        String code = "INVALID";
        when(reservationRepository.existsByCode(code)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> reservationService.getReservationByCode(code));
    }

    @Test
    void testCheckInReservation_Success() {
        String code = "CHECKIN1";
        Reservation reservation = new Reservation(code, 1, LocalDateTime.now(), 2);
        reservation.setClosed(false);

        when(reservationRepository.existsByCode(code)).thenReturn(true);
        when(reservationRepository.findByCode(code)).thenReturn(reservation);

        boolean result = reservationService.checkInReservation(code);

        assertTrue(result);
        assertTrue(reservation.isClosed());
        verify(reservationRepository).save(reservation);
    }

    @Test
    void testDeleteByCode_Success() {
        String code = "DEL1";
        Reservation reservation = new Reservation(code, 1, LocalDateTime.now(), 2);

        when(reservationRepository.existsByCode(code)).thenReturn(true);
        when(reservationRepository.findByCode(code)).thenReturn(reservation);

        boolean result = reservationService.deleteByCode(code);

        assertTrue(result);
        verify(reservationRepository).delete(reservation);
    }

    @Test
    void testUpdateReservation_Success() {
        String code = "UPD1";
        Meal meal = new Meal("Taco", 12.5, "Delicious cheesy pizza", 1);
        List<Meal> meals = List.of(meal);
        Reservation reservation = new Reservation(code, 1, LocalDateTime.now(), 2);

        when(reservationRepository.existsByCode(code)).thenReturn(true);
        when(reservationRepository.findByCode(code)).thenReturn(reservation);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> i.getArgument(0));

        Reservation updated = reservationService.updateReservation(code, meals, LocalDateTime.now(), 2);

        assertNotNull(updated);
        assertEquals(1, updated.getMealReservations().size());
    }

    @Test
    void testGetReservationsByRestaurant_Success() {
        Long restaurantId = 1L;
        Reservation reservation = new Reservation("GET123", 1, LocalDateTime.now(), 2);

        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);
        when(reservationRepository.findByRestaurantId(restaurantId)).thenReturn(List.of(reservation));

        List<Reservation> result = reservationService.getReservationsByRestaurant(restaurantId);

        assertEquals(1, result.size());
    }

    @Test
    void testGetReservationsByRestaurant_NotFound_ThrowsException() {
        when(restaurantRepository.existsById(1L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> reservationService.getReservationsByRestaurant(1L));
    }
}
