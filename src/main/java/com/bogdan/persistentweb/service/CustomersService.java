package com.bogdan.persistentweb.service;

import com.bogdan.persistentweb.domain.Customer;
import com.bogdan.persistentweb.repository.CustomersRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CustomersService {

    private final CustomersRepository repository;

    public CustomersService(CustomersRepository repository) {
        this.repository = repository;
    }

    public List<Customer> getCustomers() {
        return (List<Customer>) repository.findAll();
    }

    /**
     * Returns created entity
     */
    public Customer create(Customer customer) {
        Customer entity = new Customer();
        entity.setName(customer.getName());
        return repository.save(entity);
    }

    /**
     * Returns retrieved entity if exists, otherwise returns Optional.empty()
     */
    public Optional<Customer> get(long id) {
        return repository.findById(id);
    }

    /**
     * Returns updated entity if exists, otherwise returns Optional.empty()
     */
    public Optional<Customer> update(Customer customer) {
        return get(customer.getId())
                .map(entity -> {
                    entity.setName(customer.getName());
                    return Optional.of(repository.save(entity));
                }).orElse(Optional.empty());
    }

    /**
     * Returns deleted entity if exists, otherwise returns Optional.empty()
     */
    public Optional<Customer> delete(long id) {
        return repository.findById(id).map(customer -> {
            repository.deleteById(id);
            return Optional.of(customer);
        }).orElse(Optional.empty());
    }
}
