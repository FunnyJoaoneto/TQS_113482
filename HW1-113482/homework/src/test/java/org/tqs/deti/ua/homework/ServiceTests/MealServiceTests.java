package org.tqs.deti.ua.homework.ServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tqs.deti.ua.homework.entities.Meal;
import org.tqs.deti.ua.homework.repository.MealRepository;
import org.tqs.deti.ua.homework.repository.RestaurantRepository;
import org.tqs.deti.ua.homework.service.MealService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private MealService mealService;

    private Meal mockMeal;

    @BeforeEach
    void setUp() {
        mockMeal = new Meal();
        mockMeal.setName("Pasta");
        mockMeal.setPrice(12.99);
        mockMeal.setRestaurantId(100);
    }

    @Test
    void testGetMealsForRestaurant() {
        when(mealRepository.findByRestaurantId(100L)).thenReturn(List.of(mockMeal));

        List<Meal> result = mealService.getMealsForRestaurant(100L);

        assertEquals(1, result.size());
        assertEquals("Pasta", result.get(0).getName());
        verify(mealRepository).findByRestaurantId(100L);
    }

    @Test
    void testGetMealById_WhenExists() {
        when(mealRepository.findById(1L)).thenReturn(Optional.of(mockMeal));

        Meal result = mealService.getMealById(1L);

        assertEquals("Pasta", result.getName());
        verify(mealRepository).findById(1L);
    }

    @Test
    void testGetMealById_WhenNotExists() {
        when(mealRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> mealService.getMealById(1L));
    }

    @Test
    void testCreateMeal_WhenRestaurantExists() {
        when(restaurantRepository.existsById(100L)).thenReturn(true);
        when(mealRepository.save(any(Meal.class))).thenAnswer(i -> i.getArgument(0));

        Meal result = mealService.createMeal(100L, "Burger", 9.99);

        assertEquals("Burger", result.getName());
        assertEquals(9.99, result.getPrice());
        assertEquals(100, result.getRestaurantId());
    }

    @Test
    void testCreateMeal_WhenRestaurantDoesNotExist() {
        when(restaurantRepository.existsById(200L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> mealService.createMeal(200L, "Pizza", 10.0));
    }

    @Test
    void testUpdateMeal_WhenExists() {
        when(mealRepository.existsById(1L)).thenReturn(true);
        when(mealRepository.findById(1L)).thenReturn(Optional.of(mockMeal));
        when(mealRepository.save(any(Meal.class))).thenAnswer(i -> i.getArgument(0));

        Meal result = mealService.updateMeal(1L, "Updated Pasta", 15.0);

        assertEquals("Updated Pasta", result.getName());
        assertEquals(15.0, result.getPrice());
    }

    @Test
    void testUpdateMeal_WhenNotExists() {
        when(mealRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> mealService.updateMeal(1L, "Anything", 1.0));
    }

    @Test
    void testDeleteMeal_WhenExists() {
        when(mealRepository.existsById(1L)).thenReturn(true);
        when(mealRepository.findById(1L)).thenReturn(Optional.of(mockMeal));

        mealService.deleteMeal(1L);

        verify(mealRepository).delete(mockMeal);
    }

    @Test
    void testDeleteMeal_WhenNotExists() {
        when(mealRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> mealService.deleteMeal(1L));
    }
}
