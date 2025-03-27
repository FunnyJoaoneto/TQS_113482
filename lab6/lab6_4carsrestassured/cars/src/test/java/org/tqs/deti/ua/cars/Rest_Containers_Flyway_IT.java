package org.tqs.deti.ua.cars;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.tqs.deti.ua.cars.data.Car;
import org.tqs.deti.ua.cars.data.CarRepository;

@SpringBootTest
@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
public class Rest_Containers_Flyway_IT {

    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:12")
            .withUsername("user")
            .withPassword("pass123")
            .withDatabaseName("test");

    @Autowired
    private CarRepository carRepository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Test
    @Order(1)
    void getCar() {
        Car car = carRepository.findAll().get(0);
        assertEquals(car.getModel(), "Mustang");
        assertEquals(car.getMaker(), "Ford");
    }

    @Test
    @Order(2)
    void updateCar() {
        Car car = carRepository.findAll().get(19);
        car.setModel("Clow, Impostor, Joke");
        carRepository.save(car);
        Car car2 = carRepository.findById(car.getCarId()).get();
        assertEquals(car2.getModel(), "Clow, Impostor, Joke");
    }

    @Test
    @Order(3)
    void deleteCar() {
        Car car = carRepository.findAll().get(19);
        carRepository.delete(car);
        Car car2 = carRepository.findById(car.getCarId()).orElse(null);
        assertEquals(car2, null);
    }
}
