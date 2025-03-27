package org.tqs.deti.ua.cars.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.tqs.deti.ua.cars.data.Car;
import org.tqs.deti.ua.cars.data.CarRepository;

@Service
public class CarManagerService {

    private final CarRepository carRepository;

    public CarManagerService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car save(Car car) {
        this.carRepository.save(car);
        return car;
    }

    public List<Car> getAllCars() {
        return this.carRepository.findAll();
    }

    public Optional<Car> getCarDetails(Long carId) {
        return Optional.ofNullable(this.carRepository.findByCarId(carId));
    }

    public List<Car> findReplacementCar(Car givenCar) {
        return this.carRepository.findByMaker(givenCar.getMaker());
    }
}
