package org.tqs.deti.ua.cars;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.tqs.deti.ua.cars.data.Car;
import org.tqs.deti.ua.cars.data.CarDTO;
import org.tqs.deti.ua.cars.service.CarManagerService;
import org.tqs.deti.ua.cars.boundary.CarController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@WebMvcTest(CarController.class)
public class RestAssuredTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private CarManagerService carManagerService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void testCreateCar() throws Exception {
        CarDTO carDTO = new CarDTO(null, "Toyota", "Corolla"); // No ID, as it's assigned by DB
        Car savedCar = new Car(1L, "Toyota", "Corolla");

        when(carManagerService.save(Mockito.any(Car.class))).thenReturn(savedCar);

        RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(carDTO))
                .when()
                .post("/api/cars/")
                .then()
                .statusCode(201)
                .body("maker", is("Toyota"));
    }

    @Test
    void testGetAllCars() throws Exception {
        List<Car> cars = List.of(
                new Car(1L, "Toyota", "Corolla"),
                new Car(2L, "Honda", "Civic"));

        when(carManagerService.getAllCars()).thenReturn(cars);

        RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("/api/cars/")
                .then()
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].maker", is("Toyota"))
                .body("[1].maker", is("Honda"));
    }

    @Test
    void testGetCarById_Found() throws Exception {
        Car car = new Car(1L, "Toyota", "Corolla");

        when(carManagerService.getCarDetails(1L)).thenReturn(Optional.of(car));

        RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("/api/cars/1")
                .then()
                .statusCode(200)
                .body("maker", is("Toyota"));
    }

    @Test
    void testGetCarById_NotFound() throws Exception {
        when(carManagerService.getCarDetails(99L)).thenReturn(Optional.empty());

        RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get("/api/cars/99")
                .then()
                .statusCode(404);
    }

}
