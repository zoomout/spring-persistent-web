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
import java.util.stream.Collectors;

import static com.bogdan.persistentweb.configuration.CachingConfig.CUSTOMER_CACHE;
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

  public Page<Customer> getCustomers(final Pageable pageable) {
    return customersRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id")));
  }

  @CachePut(CUSTOMER_CACHE)
  public Customer create(Customer customer) {
    final Customer entity = new Customer();
    entity.setName(customer.getName());
    return customersRepository.save(entity);
  }

  @Cacheable(CUSTOMER_CACHE)
  public Customer get(long id) {
    return customersRepository.findById(id).orElseThrow(customerNotFoundException(id));
  }

  @CachePut(CUSTOMER_CACHE)
  public Customer update(Customer customer) {
    final Customer customerToUpdate = get(customer.getId());
    customerToUpdate.setName(customer.getName());
    return customersRepository.save(customerToUpdate);
  }

  @CacheEvict(CUSTOMER_CACHE)
  public void delete(long id) {
    final Customer customer = get(id);
    customer.getProducts().forEach(p -> p.removeCustomer(customer));
    customersRepository.deleteById(customer.getId());
  }

  @CachePut(CUSTOMER_CACHE)
  public void addProducts(long customerId, Set<Long> productsIds) {
    final Customer customer = get(customerId);
    productsIds.stream()
        .map(id -> productsRepository.findById(id).orElseThrow(productNotFoundException(id)))
        .forEach(customer::addProduct);
    customersRepository.save(customer);
  }

  @CachePut(CUSTOMER_CACHE)
  public Customer removeProducts(long customerId, Set<Long> productsIds) {
    final Customer customer = get(customerId);
    Set<Product> productsToRemove = customer.getProducts().stream()
        .filter(p -> productsIds.contains(p.getId())).collect(Collectors.toSet());
    productsToRemove.forEach(customer::removeProduct);
    return customersRepository.save(customer);
  }

}
