package org.tqs.deti.ua.homework.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tqs.deti.ua.homework.entities.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Reservation findByCode(String code);

    boolean existsByCode(String code);

    List<Reservation> findByRestaurantId(Long restaurantId);

    List<Reservation> findByRestaurantIdAndDateTime(Long restaurantId, LocalDateTime dateTime);

}
