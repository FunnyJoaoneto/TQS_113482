package org.tqs.deti.ua.cars.boundary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tqs.deti.ua.cars.data.Car;
import org.tqs.deti.ua.cars.data.CarDTO;
import org.tqs.deti.ua.cars.service.CarManagerService;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    @Autowired
    private CarManagerService carManagerService;

    @PostMapping("/")
    public ResponseEntity<Car> createCar(@RequestBody CarDTO car) {
        HttpStatus status = HttpStatus.CREATED;
        Car saved = carManagerService.save(car.toCarEntity());
        return new ResponseEntity<>(saved, status);
    }

    @GetMapping(path = "/")
    public List<Car> getAllCars() {
        return carManagerService.getAllCars();
    }

    @GetMapping(path = "/{carId}")
    public ResponseEntity<Car> getCarById(@PathVariable Long carId) {
        return carManagerService.getCarDetails(carId)
                .map(car -> new ResponseEntity<>(car, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
