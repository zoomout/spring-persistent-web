package com.bogdan.persistentweb.repository;

import com.bogdan.persistentweb.domain.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends CrudRepository<Product, Long> {
}