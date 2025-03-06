package org.tqs.deti.ua.cars;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.tqs.deti.ua.cars.data.Car;
import org.tqs.deti.ua.cars.data.CarRepository;
import org.tqs.deti.ua.cars.service.CarManagerService;

class CarManagerServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarManagerService carManagerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveCar() {
        Car car = new Car(1L, "Toyota", "Corolla");

        when(carRepository.save(car)).thenReturn(car);

        Car savedCar = carManagerService.save(car);

        assertNotNull(savedCar);
        assertEquals(car, savedCar);
        verify(carRepository, times(1)).save(car);
    }

    @Test
    void testGetAllCars() {
        List<Car> carList = List.of(new Car(1L, "Toyota", "Corolla"), new Car(2L, "Honda", "Civic"));

        when(carRepository.findAll()).thenReturn(carList);

        List<Car> result = carManagerService.getAllCars();

        assertEquals(2, result.size());
        assertEquals("Toyota", result.get(0).getMaker());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void testGetCarByIdFound() {
        Car car = new Car(1L, "Toyota", "Corolla");

        when(carRepository.findByCarId(1L)).thenReturn(car);

        Optional<Car> result = carManagerService.getCarDetails(1L);

        assertTrue(result.isPresent());
        assertEquals(car, result.get());
        verify(carRepository, times(1)).findByCarId(1L);
    }

    @Test
    void testGetCarByIdNotFound() {
        when(carRepository.findByCarId(99L)).thenReturn(null);

        Optional<Car> result = carManagerService.getCarDetails(99L);

        assertTrue(result.isEmpty());
        verify(carRepository, times(1)).findByCarId(99L);
    }

    @Test
    void testFindReplacementCar() {
        Car givenCar = new Car(1L, "Toyota", "Corolla");
        List<Car> toyotaCars = List.of(
                new Car(2L, "Toyota", "Camry"),
                new Car(3L, "Toyota", "Yaris"));

        when(carRepository.findByMaker("Toyota")).thenReturn(toyotaCars);

        List<Car> result = carManagerService.findReplacementCar(givenCar);

        assertEquals(2, result.size());
        assertEquals("Toyota", result.get(0).getMaker());
        verify(carRepository, times(1)).findByMaker("Toyota");
    }
}
