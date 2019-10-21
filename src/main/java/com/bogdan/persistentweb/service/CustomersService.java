package com.bogdan.persistentweb.service;

import com.bogdan.persistentweb.domain.BaseEntity;
import com.bogdan.persistentweb.domain.Customer;
import com.bogdan.persistentweb.domain.Product;
import com.bogdan.persistentweb.repository.CustomersRepository;
import com.bogdan.persistentweb.repository.ProductsRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bogdan.persistentweb.exception.ExceptionsSuppliers.customerNotFoundException;
import static com.bogdan.persistentweb.exception.ExceptionsSuppliers.productNotFoundException;

@Component
public class CustomersService {

    private final CustomersRepository customersRepository;
    private final ProductsRepository productsRepository;

    public CustomersService(
            CustomersRepository customersRepository,
            ProductsRepository productsRepository
    ) {
        this.customersRepository = customersRepository;
        this.productsRepository = productsRepository;
    }

    public List<Customer> getCustomers() {
        return (List<Customer>) customersRepository.findAll();
    }

    public Customer create(Customer customer) {
        Customer entity = new Customer();
        entity.setName(customer.getName());
        return customersRepository.save(entity);
    }

    public Customer get(long id) {
        return customersRepository.findById(id).orElseThrow(customerNotFoundException(id));
    }

    public void update(Customer customer) {
        final Customer customerToUpdate = get(customer.getId());
        customerToUpdate.setName(customer.getName());
        customersRepository.save(customerToUpdate);
    }

    public void delete(long id) {
        unlinkAllProducts(id);
        customersRepository.deleteById(get(id).getId());
    }

    public void linkProducts(long customerId, Set<Long> productsIds) {
        final Customer customer = get(customerId);
        customer.setProducts(productsIds.stream()
                .map(id -> productsRepository.findById(id).orElseThrow(productNotFoundException(id)))
                .collect(Collectors.toSet()));
        customersRepository.save(customer);
    }

    public void unlinkProducts(long customerId, Set<Long> productsIds) {
        final Customer customer = get(customerId);
        final Set<Product> productsWithoutRemovedOnes = customer.getProducts().stream()
                .filter(p -> !productsIds.contains(p.getId())).collect(Collectors.toSet());
        customer.setProducts(productsWithoutRemovedOnes);
        customersRepository.save(customer);
    }

    private void unlinkAllProducts(long customerId) {
        final Customer customer = get(customerId);
        customer.setProducts(new HashSet<>());
        customersRepository.save(customer);
    }

}
