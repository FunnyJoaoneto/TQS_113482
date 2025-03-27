package org.tqs.deti.ua.cars.data;

import java.util.Objects;

public class CarDTO {
    private Long carId;
    private String maker;
    private String model;

    public CarDTO() {
    }

    public CarDTO(Long carId, String maker, String model) {
        this.carId = carId;
        this.maker = maker;
        this.model = model;
    }

    public static CarDTO fromCarEntity(Car car) {
        return new CarDTO(car.getCarId(), car.getMaker(), car.getModel());
    }

    public Car toCarEntity() {
        return new Car(this.carId, this.maker, this.model);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        CarDTO carDTO = (CarDTO) obj;
        return Objects.equals(carId, carDTO.carId) &&
                Objects.equals(maker, carDTO.maker) &&
                Objects.equals(model, carDTO.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carId, maker, model);
    }

    @Override
    public String toString() {
        return "CarDTO{" +
                "carId=" + carId +
                ", maker='" + maker + '\'' +
                ", model='" + model + '\'' +
                '}';
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
