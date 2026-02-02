package com.insurance.auto.adapter.out.persistence;

import com.insurance.auto.adapter.out.persistence.entity.CarJpaEntity;
import com.insurance.auto.adapter.out.persistence.entity.DriverJpaEntity;
import com.insurance.auto.adapter.out.persistence.repository.CarJpaRepository;
import com.insurance.auto.adapter.out.persistence.repository.DriverJpaRepository;
import com.insurance.auto.application.port.out.LoadCarPort;
import com.insurance.auto.application.port.out.LoadDriverPort;
import com.insurance.auto.application.port.out.SaveCarPort;
import com.insurance.auto.application.port.out.SaveDriverPort;
import com.insurance.auto.domain.model.Car;
import com.insurance.auto.domain.model.Driver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InsuredPersistenceAdapter implements SaveDriverPort, SaveCarPort, LoadDriverPort, LoadCarPort {

    private final DriverJpaRepository driverRepository;
    private final CarJpaRepository carRepository;

    @Override
    public Driver save(Driver driver) {
        DriverJpaEntity entity = new DriverJpaEntity(
                driver.name(),
                driver.birthDate(),
                driver.phone(),
                driver.accidentHistoryCount()
        );

        DriverJpaEntity savedEntity = driverRepository.save(entity);

        return new Driver(
                savedEntity.getId(),
                savedEntity.getName(),
                savedEntity.getBirthDate(),
                savedEntity.getAccidentHistoryCount(),
                savedEntity.getPhone()
        );
    }

    @Override
    public Car save(Car car) {
        CarJpaEntity entity = new CarJpaEntity(
                car.carNumber(),
                car.modelName(),
                car.subModelName(),
                car.modelYear(),
                car.price(),
                car.hasBlackBox()
        );

        CarJpaEntity savedEntity = carRepository.save(entity);

        return new Car(
                savedEntity.getId(),
                savedEntity.getCarNumber(),
                savedEntity.getModelName(),
                savedEntity.getSubModelName(),
                savedEntity.getModelYear(),
                savedEntity.getPrice(),
                savedEntity.isHasBlackBox()
        );
    }

    @Override
    public Optional<Driver> loadDriver(Long driverId) {
        return driverRepository.findById(driverId)
                .map(entity -> new Driver(
                        entity.getId(),
                        entity.getName(),
                        entity.getBirthDate(),
                        entity.getAccidentHistoryCount(),
                        entity.getPhone()
                ));
    }

    @Override
    public Optional<Driver> loadDriver(String name, LocalDate birthDate, String phone) {
        return driverRepository.findByNameAndBirthDateAndPhone(name, birthDate, phone)
                .map(entity -> new Driver(
                        entity.getId(),
                        entity.getName(),
                        entity.getBirthDate(),
                        entity.getAccidentHistoryCount(),
                        entity.getPhone()
                ));
    }

    @Override
    public Optional<Car> loadCar(Long carId) {
        return carRepository.findById(carId)
                .map(entity -> new Car(
                        entity.getId(),
                        entity.getCarNumber(),
                        entity.getModelName(),
                        entity.getSubModelName(),
                        entity.getModelYear(),
                        entity.getPrice(),
                        entity.isHasBlackBox()
                ));
    }
}
