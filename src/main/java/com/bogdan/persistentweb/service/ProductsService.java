package com.bogdan.persistentweb.service;

import com.bogdan.persistentweb.domain.Customer;
import com.bogdan.persistentweb.domain.Product;
import com.bogdan.persistentweb.repository.CustomersRepository;
import com.bogdan.persistentweb.repository.ProductsRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Stream;

import static com.bogdan.persistentweb.configuration.CachingConfig.PRODUCT_CACHE;
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

  public Page<Product> getProducts(final Pageable pageable) {
    return productsRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id")));
  }

  @CachePut(PRODUCT_CACHE)
  public Product create(Product product) {
    final Product entity = new Product();
    entity.setTitle(product.getTitle());
    return productsRepository.save(entity);
  }

  @Cacheable(PRODUCT_CACHE)
  public Product get(long id) {
    return productsRepository.findById(id).orElseThrow(productNotFoundException(id));
  }

  @CachePut(PRODUCT_CACHE)
  public void update(Product product) {
    final Product productToUpdate = get(product.getId());
    productToUpdate.setTitle(product.getTitle());
    productsRepository.save(productToUpdate);
  }

  @CacheEvict(PRODUCT_CACHE)
  public void delete(long id) {
    final Product product = get(id);
    product.getCustomers().forEach(c -> c.removeProduct(product));
    productsRepository.deleteById(product.getId());
  }

  @CachePut(PRODUCT_CACHE)
  public void addCustomers(long productId, Set<Long> customersIds) {
    final Product product = get(productId);
    customersIds.stream()
        .map(id -> customersRepository.findById(id).orElseThrow(customerNotFoundException(id)))
        .forEach(product::addCustomer);
    productsRepository.save(product);
  }

  @CachePut(PRODUCT_CACHE)
  public Product removeCustomers(long productId, Set<Long> customersIds) {
    final Product product = get(productId);
    final Stream<Customer> customersToRemove = product.getCustomers().stream()
        .filter(c -> customersIds.contains(c.getId()));
    customersToRemove.forEach(product::removeCustomer);
    return productsRepository.save(product);
  }

}
