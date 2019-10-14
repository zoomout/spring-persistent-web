package com.bogdan.persistentweb.mapper;

import com.bogdan.persistentweb.domain.Customer;
import com.bogdan.persistentweb.dto.CustomerDto;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerDto toDto(final Customer customer) {
        if (customer == null) {
            return null;
        }
        return new CustomerDto(String.valueOf(customer.getId()), customer.getName());
    }

    public Customer toEntity(final CustomerDto customerDto) {
        return toEntity(null, customerDto);
    }

    public Customer toEntity(final Long id, final CustomerDto customerDto) {
        if (customerDto == null) {
            return null;
        }
        Customer customer = new Customer();
        if (id != null) {
            customer.setId(id);
        }
        customer.setName(customerDto.getName());
        return customer;
    }
}
