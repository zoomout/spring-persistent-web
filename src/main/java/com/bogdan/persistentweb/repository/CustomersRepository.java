package com.bogdan.persistentweb.repository;

import com.bogdan.persistentweb.domain.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomersRepository extends CrudRepository<Customer, Long> {
}