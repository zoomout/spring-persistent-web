package com.bogdan.persistentweb.web;

import com.bogdan.persistentweb.domain.Customer;
import com.bogdan.persistentweb.dto.CustomerDto;
import com.bogdan.persistentweb.mapper.CustomerMapper;
import com.bogdan.persistentweb.service.CustomersService;
import com.bogdan.persistentweb.validation.ValidLong;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.bogdan.persistentweb.utils.UriUtil.createUri;

@RestController
@RequestMapping("/customers")
@Validated
public class CustomersController {

    private final CustomersService customersService;
    private final CustomerMapper customerMapper;

    public CustomersController(CustomersService customersService, CustomerMapper customerMapper) {
        this.customersService = customersService;
        this.customerMapper = customerMapper;
    }

    @GetMapping
    public List<CustomerDto> getCustomers() {
        return customersService
                .getCustomers().stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<CustomerDto> postCustomer(@Valid @RequestBody CustomerDto customerDto) {
        final Customer createdCustomer = customersService.create(customerMapper.toEntity(customerDto));
        return ResponseEntity.created(createUri("/customers/" + createdCustomer.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@ValidLong @PathVariable String id) {
        return customersService
                .get(Long.parseLong(id))
                .map(customer -> ResponseEntity.ok().body(customerMapper.toDto(customer)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> putCustomer(@ValidLong @PathVariable String id,
                                                   @Valid @RequestBody CustomerDto customerDto) {
        return customersService
                .update(customerMapper.toEntity(Long.parseLong(id), customerDto))
                .map(customer -> ResponseEntity.ok().body(customerMapper.toDto(customer)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCustomer(@ValidLong @PathVariable String id) {
        return customersService
                .delete(Long.parseLong(id))
                .map(ignored -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }

}