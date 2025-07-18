package com.bank.denominations.controller;

import com.bank.denominations.model.Denomination;
import com.bank.denominations.model.DenominationRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/denomination")
public class DenominationController {
    private final DenominationRepository denominationRepository;

    public DenominationController(DenominationRepository denominationRepository) {
        this.denominationRepository = denominationRepository;
    }

    @GetMapping
    public List<Denomination> getDenominations() {
        return denominationRepository.findAll();
    }

    @GetMapping("/{id}")
    public Denomination getDenomination(@PathVariable Integer id) {
        return denominationRepository.findById(id).orElseThrow(DenominationNotFoundException::new);
    }

    @PostMapping
    public Denomination create(@RequestBody @Valid Denomination denomination) {
        return denominationRepository.save(denomination);
    }

    @PutMapping("/{id}")
    public Denomination update(@PathVariable Integer id, @RequestBody Denomination denomination) {
        Optional<Denomination> optionalDenomination = denominationRepository.findById(id);
        if (optionalDenomination.isPresent()) {
            Denomination denominationToUpdate = Denomination.builder()
                    .id(id)
                    .name(denomination.name())
                    .value(denomination.value())
                    .currency(denomination.currency())
                    .coin(denomination.coin())
                    .baggedQuantity(denomination.baggedQuantity())
                    .bundledQuantity(denomination.bundledQuantity())
                    .plasticQuantity(denomination.plasticQuantity())
                    .version(denomination.version())
                    .build();
            return denominationRepository.save(denominationToUpdate);
        } else {
            throw new DenominationNotFoundException();
        }
    }
}
