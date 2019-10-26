package com.bogdan.persistentweb.repository;

import com.bogdan.persistentweb.domain.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomersRepository extends PagingAndSortingRepository<Customer, Long> {
}