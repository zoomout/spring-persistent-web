package com.bogdan.persistentweb.service;

import com.bogdan.persistentweb.domain.Product;
import com.bogdan.persistentweb.repository.ProductsRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductsService {

    private final ProductsRepository repository;

    public ProductsService(ProductsRepository repository) {
        this.repository = repository;
    }

    public List<Product> getProducts() {
        return (List<Product>) repository.findAll();
    }

    /**
     * Returns created entity
     */
    public Product create(Product Product) {
        Product entity = new Product();
        entity.setTitle(Product.getTitle());
        return repository.save(entity);
    }

    /**
     * Returns retrieved entity if exists, otherwise returns Optional.empty()
     */
    public Optional<Product> get(long id) {
        return repository.findById(id);
    }

    /**
     * Returns updated entity if exists, otherwise returns Optional.empty()
     */
    public Optional<Product> update(Product Product) {
        return get(Product.getId())
                .map(entity -> {
                    entity.setTitle(Product.getTitle());
                    return Optional.of(repository.save(entity));
                }).orElse(Optional.empty());
    }

    /**
     * Returns deleted entity if exists, otherwise returns Optional.empty()
     */
    public Optional<Product> delete(long id) {
        return repository.findById(id).map(Product -> {
            repository.deleteById(id);
            return Optional.of(Product);
        }).orElse(Optional.empty());
    }
}
