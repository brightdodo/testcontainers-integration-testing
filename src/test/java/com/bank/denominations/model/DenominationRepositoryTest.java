package com.bank.denominations.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJdbcTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class DenominationRepositoryTest {
    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4.5");

    @Autowired
    private DenominationRepository denominationRepository;

    @Test
    void testConnection() {
        assertTrue(mysql.isCreated());
        assertTrue(mysql.isRunning());
    }

    @Test
    @Rollback
    void shouldFindDenominationByName() {
        var denoms  = List.of(new Denomination(3111, "500", "ZAR", BigDecimal.valueOf(500L), false, 200, null, null, null));
        denominationRepository.saveAll(denoms);

        Denomination denomination = denominationRepository.findByName("500");
        assertThat(denomination).isNotNull();
        assertThat(denomination.currency()).isEqualTo("ZAR");
    }
}