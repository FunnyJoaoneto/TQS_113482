package org.tqs.deti.ua.homework.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import jakarta.persistence.CascadeType;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", unique = true)
    private String code;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<MealReservation> mealReservations = new ArrayList<>();

    @Column(name = "restaurant_id")
    private Integer restaurantId;

    @Column(name = "datetime")
    private LocalDateTime dateTime;

    @Column(name = "people")
    private Integer people;

    @Column(name = "closed")
    private boolean closed;

    public Reservation() {
        this.closed = false;
    }

    public Reservation(String code, Integer restaurantId, LocalDateTime dateTime, Integer people) {
        this.code = code;
        this.restaurantId = restaurantId;
        this.dateTime = dateTime;
        this.people = people;
        this.closed = false;
    }

    public Integer getId() {
        return id;
    }

    public boolean isClosed() {
        return closed;
    }

    public String getCode() {
        return code;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Integer getPeople() {
        return people;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setPeople(Integer people) {
        this.people = people;
    }

    public List<MealReservation> getMealReservations() {
        return mealReservations;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setMealReservations(List<MealReservation> mealReservations) {
        this.mealReservations = mealReservations;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", restaurantId=" + restaurantId +
                ", dateTime=" + dateTime +
                ", people=" + people +
                ", closed=" + closed +
                '}';
    }
}
