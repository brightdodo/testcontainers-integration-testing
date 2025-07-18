package com.bank.denominations.model;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DenominationRepository extends ListCrudRepository<Denomination, Integer> {
}
