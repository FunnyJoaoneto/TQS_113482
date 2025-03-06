package org.tqs.deti.ua.cars.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    public Car findByCarId(Long carId);

    @Override
    public List<Car> findAll();

    @Query("SELECT c FROM Car c WHERE c.maker LIKE %:maker%")
    public List<Car> findByMaker(String maker);
}
