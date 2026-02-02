package com.insurance.auto.adapter.out.persistence.repository;

import com.insurance.auto.adapter.out.persistence.entity.DriverJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DriverJpaRepositoryTest {

    @Autowired DriverJpaRepository driverRepository;

    @Test
    @DisplayName("이름, 생일, 폰번호로 운전자를 찾을 수 있다")
    void find_by_info() {
        // Given
        DriverJpaEntity entity = new DriverJpaEntity("테스터", LocalDate.of(1990, 1, 1), "010-1234-5678", 0);
        driverRepository.save(entity);

        // When
        Optional<DriverJpaEntity> result = driverRepository.findByNameAndBirthDateAndPhone("테스터", LocalDate.of(1990, 1, 1), "010-1234-5678");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("테스터");
    }

    @Test
    @DisplayName("정보가 다르면 운전자를 찾을 수 없다")
    void find_by_info_fail() {
        // Given
        driverRepository.save(new DriverJpaEntity("테스터", LocalDate.of(1990, 1, 1), "010-1234-5678", 0));

        // When
        Optional<DriverJpaEntity> result = driverRepository.findByNameAndBirthDateAndPhone("테스터", LocalDate.of(1990, 1, 1), "010-9999-9999");

        // Then
        assertThat(result).isEmpty();
    }
}
