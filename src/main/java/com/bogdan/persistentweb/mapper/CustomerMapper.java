package com.bogdan.persistentweb.mapper;

import com.bogdan.persistentweb.domain.Customer;
import com.bogdan.persistentweb.dto.CustomerDto;

public class CustomerMapper {

  public static CustomerDto toDto(final Customer customer) {
    return new CustomerDto(String.valueOf(customer.getId()), customer.getName());
  }

  public static Customer toEntity(final CustomerDto customerDto) {
    return toEntity(null, customerDto);
  }

  public static Customer toEntity(final Long id, final CustomerDto customerDto) {
    Customer customer = new Customer();
    if (id != null) {
      customer.setId(id);
    }
    customer.setName(customerDto.getName());
    return customer;
  }
}
