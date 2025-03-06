package org.tqs.deti.ua.cars;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.tqs.deti.ua.cars.data.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class APIRepositoryIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CarRepository carRepository;

    @AfterEach
    public void resetDb() {
        carRepository.deleteAll();
    }

    @Test
    void CreateCar() {
        Car car = new Car("Ford", "Mustang");
        restTemplate.postForEntity("/api/cars/", car, Car.class);

        List<Car> cars = carRepository.findAll();
        assertThat(cars).extracting(Car::getMaker).containsOnly("Ford");
    }

    @Test
    void GetCars() {
        Car car = new Car("Ford", "Mustang");
        carRepository.saveAndFlush(car);
        Car car1 = new Car("Ford", "Raptor");
        carRepository.saveAndFlush(car1);

        ResponseEntity<List<Car>> response = restTemplate.exchange("/api/cars/", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Car>>() {
                });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2).extracting(Car::getMaker).contains("Ford", "Ford");
    }
}