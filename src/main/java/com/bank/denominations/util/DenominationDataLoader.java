package com.bank.denominations.util;

import com.bank.denominations.model.Denomination;
import com.bank.denominations.model.DenominationRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DenominationDataLoader implements CommandLineRunner {
    private final DenominationRepository denominationRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        if (denominationRepository.count() == 0) {
            String denominationsJson = "/data/denominations.json";
            log.info("Reading denominations from {}", denominationsJson);
            try (InputStream inputStream = TypeReference.class.getResourceAsStream(denominationsJson)) {
                List<Denomination> denominations = objectMapper.readValue(inputStream, new TypeReference<List<Denomination>>() {});
                denominationRepository.saveAll(denominations);
            } catch (IOException e) {
                log.error("Error reading denominations from {}", denominationsJson, e);
            }
        }
    }
}
