package com.bank.denominations.controller;

import com.bank.denominations.model.Denomination;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DenominationControllerTest {
    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4.5");

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    void shouldFindAllDenominations() {
        Denomination[] demons = testRestTemplate.getForObject("/api/denomination", Denomination[].class);
        assertThat(demons).isNotEmpty();
        assertThat(demons.length).isEqualTo(227);
    }

    @Test
    void shouldFindDenominationWhenValidId() {
        var result = testRestTemplate.exchange("/api/denomination/311", HttpMethod.GET, null, Denomination.class);
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
    }

    @Test
    void shouldCreateDenominationWhenValid() {
        Denomination denomination = new Denomination(3110, "500", "ZAR", BigDecimal.valueOf(500L), false, 200, null, null, null);

        ResponseEntity<Denomination> response = testRestTemplate.postForEntity("/api/denomination", denomination, Denomination.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(3110);
        assertThat(response.getBody().name()).isEqualTo("500");
    }

    @Test
    void shouldNotCreateDenominationWhenInvalid() {
        Denomination denomination = new Denomination(3110, "500", "ZAR", null, false, 200, null, null, null);

        ResponseEntity<Denomination> response = testRestTemplate.postForEntity("/api/denomination", denomination, Denomination.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldUpdateDenominationWhenValid() {
        ResponseEntity<Denomination> response = testRestTemplate.exchange("/api/denomination/311", HttpMethod.GET, null, Denomination.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Denomination existing = response.getBody();
        assertThat(existing).isNotNull();

        Denomination denom = new Denomination(existing.id(), "500", "ZAR", BigDecimal.valueOf(500L), false, 200, null, null, existing.version());
        RequestEntity requestEntity = new RequestEntity(denom, HttpMethod.PUT, URI.create("/api/denomination/311"));
        ResponseEntity<Denomination> updated = testRestTemplate.exchange("/api/denomination/311", HttpMethod.PUT, requestEntity, Denomination.class);
        assertThat(updated).isNotNull();
        assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updated.getBody()).isNotNull();
        assertThat(updated.getBody().id()).isEqualTo(311);
        assertThat(updated.getBody().name()).isEqualTo("500");
    }
}