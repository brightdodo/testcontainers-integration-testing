package com.bank.denominations.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.math.BigDecimal;

@Builder
public record Denomination(
        @Id
        Integer id,
        String name,
        String currency,
        @NotNull
        BigDecimal value,
        boolean coin,
        Integer bundledQuantity,
        Integer baggedQuantity,
        Integer plasticQuantity,
        @Version
        Long version
) {
}
