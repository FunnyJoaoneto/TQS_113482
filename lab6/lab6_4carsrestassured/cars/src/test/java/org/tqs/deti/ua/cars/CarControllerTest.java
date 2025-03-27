package org.tqs.deti.ua.cars;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.tqs.deti.ua.cars.boundary.CarController;
import org.tqs.deti.ua.cars.data.Car;
import org.tqs.deti.ua.cars.data.CarDTO;
import org.tqs.deti.ua.cars.service.CarManagerService;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private CarManagerService carManagerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateCar() throws Exception {
        CarDTO carDTO = new CarDTO(null, "Toyota", "Corolla"); // No ID, as it's assigned by DB
        Car savedCar = new Car(1L, "Toyota", "Corolla");

        when(carManagerService.save(Mockito.any(Car.class))).thenReturn(savedCar);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cars/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.carId").value(1))
                .andExpect(jsonPath("$.maker").value("Toyota"))
                .andExpect(jsonPath("$.model").value("Corolla"));

        verify(carManagerService, times(1)).save(Mockito.any(Car.class));
    }

    @Test
    void testGetAllCars() throws Exception {
        List<Car> cars = List.of(
                new Car(1L, "Toyota", "Corolla"),
                new Car(2L, "Honda", "Civic"));

        when(carManagerService.getAllCars()).thenReturn(cars);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cars/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].maker").value("Toyota"))
                .andExpect(jsonPath("$[1].maker").value("Honda"));

        verify(carManagerService, times(1)).getAllCars();
    }

    @Test
    void testGetCarById_Found() throws Exception {
        Car car = new Car(1L, "Toyota", "Corolla");

        when(carManagerService.getCarDetails(1L)).thenReturn(Optional.of(car));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cars/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carId").value(1))
                .andExpect(jsonPath("$.maker").value("Toyota"))
                .andExpect(jsonPath("$.model").value("Corolla"));
    }

    @Test
    void testGetCarById_NotFound() throws Exception {
        when(carManagerService.getCarDetails(99L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cars/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
