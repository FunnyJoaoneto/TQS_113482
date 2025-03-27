package org.tqs.deti.ua.cars;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.tqs.deti.ua.cars.data.Car;
import org.tqs.deti.ua.cars.data.CarRepository;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc
public class Rest_Containers_Flyway_IT {

    @Autowired
    private CarRepository carRepository;

    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:12")
            .withUsername("user")
            .withPassword("pass123")
            .withDatabaseName("test");

    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @Order(1)
    void createCar() {
        Car car = new Car("Toyota", "Yaris");
        RestAssuredMockMvc.given()
                .contentType("application/json")
                .body(car)
                .when()
                .post("/api/cars/")
                .then()
                .statusCode(201);
        Car car1 = carRepository.findAll().get(20);
        assertEquals(car.getMaker(), car1.getMaker());
    }

    @Test
    @Order(2)
    void givenCars_whenGetCars_thenStatus200() {
        RestAssuredMockMvc.given()
                .contentType("application/json")
                .when()
                .get("/api/cars/")
                .then()
                .statusCode(200)
                .body("size()", is(21))
                .body("[0].maker", is("Ford"))
                .body("[1].maker", is("Ford"))
                .body("[2].maker", is("Lamborghini"));
    }
}
