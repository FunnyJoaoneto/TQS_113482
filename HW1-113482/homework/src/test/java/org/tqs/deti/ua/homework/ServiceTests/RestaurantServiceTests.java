package org.tqs.deti.ua.homework.ServiceTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tqs.deti.ua.homework.entities.Meal;
import org.tqs.deti.ua.homework.entities.Reservation;
import org.tqs.deti.ua.homework.entities.Restaurant;
import org.tqs.deti.ua.homework.repository.MealRepository;
import org.tqs.deti.ua.homework.repository.ReservationRepository;
import org.tqs.deti.ua.homework.repository.RestaurantRepository;
import org.tqs.deti.ua.homework.service.RestaurantService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private MealRepository mealRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    void whenGetAllRestaurants_thenReturnListOfRestaurants() {
        Restaurant r1 = new Restaurant("Sushi House", "Lisbon", 1010500, 50, "10:00", "23:00");
        Restaurant r2 = new Restaurant("Pasta Palace", "Porto", 4000000, 40, "11:00", "22:00");
        when(restaurantRepository.findAll()).thenReturn(List.of(r1, r2));

        List<Restaurant> restaurants = restaurantService.getAllRestaurants();

        assertEquals(2, restaurants.size());
        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    void whenGetRestaurantById_thenReturnRestaurant() {
        Restaurant r = new Restaurant("Sushi House", "Lisbon", 1010500, 50, "10:00", "23:00");
        when(restaurantRepository.existsById(1L)).thenReturn(true); // <-- this line is crucial
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));

        Restaurant result = restaurantService.getRestaurantById(1L);

        assertEquals("Sushi House", result.getName());
        verify(restaurantRepository, times(1)).existsById(1L); // optional, for good practice
        verify(restaurantRepository, times(1)).findById(1L);
    }

    @Test
    void whenGetMealsByRestaurantId_andRestaurantExists_thenReturnMeals() {
        when(restaurantRepository.existsById(1L)).thenReturn(true);
        Meal m1 = new Meal("Pizza", 12.5, "Delicious cheesy pizza", 1);
        Meal m2 = new Meal("Burguer", 12.5, "Delicious cheesy burguer", 1);
        when(mealRepository.findByRestaurantId(1L)).thenReturn(List.of(m1, m2));

        List<Meal> meals = restaurantService.getMealsByRestaurantId(1L);

        assertEquals(2, meals.size());
        verify(mealRepository, times(1)).findByRestaurantId(1L);
    }

    @Test
    void whenGetMealsByRestaurantId_andRestaurantDoesNotExist_thenThrowException() {
        when(restaurantRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> restaurantService.getMealsByRestaurantId(1L));
    }

    @Test
    void whenGetReservationsByRestaurantId_andRestaurantExists_thenReturnReservations() {
        when(restaurantRepository.existsById(1L)).thenReturn(true);
        Reservation r1 = new Reservation("RES1234", 1, LocalDateTime.now().plusDays(1), 2);
        when(reservationRepository.findByRestaurantId(1L)).thenReturn(List.of(r1));

        List<Reservation> reservations = restaurantService.getReservationsByRestaurantId(1L);

        assertEquals(1, reservations.size());
        verify(reservationRepository, times(1)).findByRestaurantId(1L);
    }

    @Test
    void whenGetReservationsByRestaurantId_andRestaurantDoesNotExist_thenThrowException() {
        when(restaurantRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> restaurantService.getReservationsByRestaurantId(1L));
    }

    @Test
    void whenCreateRestaurant_thenRestaurantShouldBeSaved() {
        Restaurant r = new Restaurant("Sushi House", "Lisbon", 1010500, 50, "10:00", "23:00");
        when(restaurantRepository.save(r))
                .thenReturn(new Restaurant("Sushi House", "Lisbon", 1010500, 50, "10:00", "23:00"));

        Restaurant saved = restaurantService.createRestaurant(r);

        assertEquals("Sushi House", saved.getName());
        verify(restaurantRepository, times(1)).save(r);
    }

    @Test
    void whenUpdateRestaurant_andRestaurantExists_thenUpdateAndReturn() {
        Restaurant existing = new Restaurant("Sushi House", "Lisbon", 1010500, 50, "10:00", "23:00");
        Restaurant updated = new Restaurant("Burguer House", "Lisbon", 1010500, 30, "10:00", "23:00");
        when(restaurantRepository.existsById(1L)).thenReturn(true);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(i -> i.getArgument(0));

        Restaurant result = restaurantService.updateRestaurant(1L, updated);

        assertEquals("Burguer House", result.getName());
        assertEquals(30, result.getCapacity());
        verify(restaurantRepository).save(existing);
    }

    @Test
    void whenUpdateRestaurant_andRestaurantDoesNotExist_thenThrowException() {
        when(restaurantRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> restaurantService.updateRestaurant(1L, new Restaurant()));
    }

    @Test
    void whenDeleteRestaurant_andRestaurantExists_thenDeleteIt() {
        Restaurant r = new Restaurant("Sushi House", "Lisbon", 1010500, 50, "10:00", "23:00");
        when(restaurantRepository.existsById(1L)).thenReturn(true);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));

        restaurantService.deleteRestaurant(1L);

        verify(restaurantRepository, times(1)).delete(r);
    }

    @Test
    void whenDeleteRestaurant_andRestaurantDoesNotExist_thenThrowException() {
        when(restaurantRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> restaurantService.deleteRestaurant(1L));
    }
}
