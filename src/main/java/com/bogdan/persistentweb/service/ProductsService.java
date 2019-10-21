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
public class ProductsService {

    private final ProductsRepository productsRepository;
    private final CustomersRepository customersRepository;

    public ProductsService(
            ProductsRepository productsRepository,
            CustomersRepository customersRepository
    ) {
        this.productsRepository = productsRepository;
        this.customersRepository = customersRepository;
    }

    public List<Product> getProducts() {
        return (List<Product>) productsRepository.findAll();
    }

    public Product create(Product product) {
        Product entity = new Product();
        entity.setTitle(product.getTitle());
        return productsRepository.save(entity);
    }

    public Product get(long id) {
        return productsRepository.findById(id).orElseThrow(productNotFoundException(id));
    }

    public void update(Product product) {
        final Product productToUpdate = get(product.getId());
        productToUpdate.setTitle(product.getTitle());
        productsRepository.save(productToUpdate);
    }

    public void delete(long id) {
        unlinkAllCustomers(id);
        productsRepository.deleteById(get(id).getId());
    }

    public void linkCustomers(long productId, Set<Long> customersIds) {
        Product product = get(productId);
        product.setCustomers(customersIds.stream()
                .map(id -> customersRepository.findById(id).orElseThrow(customerNotFoundException(id)))
                .collect(Collectors.toSet()));
        productsRepository.save(product);
    }

    public void unlinkCustomers(long productId, Set<Long> customersIds) {
        final Product product = get(productId);
        final Set<Customer> customersWithoutRemovedOnes = product.getCustomers().stream()
                .filter(p -> !customersIds.contains(p.getId())).collect(Collectors.toSet());
        product.setCustomers(customersWithoutRemovedOnes);
        productsRepository.save(product);
    }

    public void unlinkAllCustomers(long productId) {
        final Product product = get(productId);
        product.setCustomers(new HashSet<>());
        productsRepository.save(product);
    }

}
