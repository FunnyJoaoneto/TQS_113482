package org.tqs.deti.ua.cars;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.tqs.deti.ua.cars.data.*;

@DataJpaTest
public class CarRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarRepository carRepository;

    @Test
    public void testFindByCarId() {
        Car car = new Car("Toyota", "Yaris");
        entityManager.persistAndFlush(car);

        Car foundCar = carRepository.findByCarId(car.getCarId());

        assertThat(foundCar).isEqualTo(car);
    }

    @Test
    public void testFindAll() {
        Car car1 = new Car("Toyota", "Yaris");
        Car car2 = new Car("Toyota", "Corolla");
        Car car3 = new Car("Ford", "Raptor");

        entityManager.persistAndFlush(car1);
        entityManager.persistAndFlush(car2);
        entityManager.persistAndFlush(car3);

        List<Car> cars = carRepository.findAll();

        assertThat(cars).hasSize(3).contains(car1, car2, car3);
    }

    @Test
    public void testFindByCarIdNotFound() {
        Car foundCar = carRepository.findByCarId(1000L);

        assertThat(foundCar).isNull();
    }
}