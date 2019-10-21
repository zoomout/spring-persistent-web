package com.bogdan.persistentweb.web;

import com.bogdan.persistentweb.domain.Customer;
import com.bogdan.persistentweb.dto.CustomerDto;
import com.bogdan.persistentweb.dto.ProductDto;
import com.bogdan.persistentweb.mapper.CustomerMapper;
import com.bogdan.persistentweb.mapper.ProductMapper;
import com.bogdan.persistentweb.service.CustomersService;
import com.bogdan.persistentweb.validation.ValidLong;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bogdan.persistentweb.utils.UriUtil.createUri;
import static java.lang.Long.parseLong;

@RestController
@RequestMapping("/customers")
@Validated
public class CustomersController {

    private final CustomersService customersService;
    private final CustomerMapper customerMapper;
    private final ProductMapper productMapper;

    public CustomersController(
            CustomersService customersService,
            CustomerMapper customerMapper,
            ProductMapper productMapper
    ) {
        this.customersService = customersService;
        this.customerMapper = customerMapper;
        this.productMapper = productMapper;
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
        return ResponseEntity.ok().body(customerMapper.toDto(customersService.get(parseLong(id))));
    }

    @PutMapping("/{id}")
    public ResponseEntity putCustomer(@ValidLong @PathVariable String id,
                                      @Valid @RequestBody CustomerDto customerDto) {
        customersService.update(customerMapper.toEntity(parseLong(id), customerDto));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCustomer(@ValidLong @PathVariable String id) {
        customersService.delete(parseLong(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<Set<ProductDto>> getCustomerProducts(@ValidLong @PathVariable String id) {
        return ResponseEntity.ok().body(
                customersService.get(parseLong(id)).getProducts().stream()
                        .sorted()
                        .map(productMapper::toDto).collect(Collectors.toCollection(LinkedHashSet::new)
                ));
    }

    @PutMapping("/{id}/products")
    public ResponseEntity putCustomerProducts(
            @ValidLong @PathVariable String id,
            @RequestBody Set<Long> productIds
    ) {
        customersService.linkProducts(parseLong(id), productIds);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/products")
    public ResponseEntity deleteCustomerProducts(
            @ValidLong @PathVariable String id,
            @RequestBody Set<Long> productIds
    ) {
        customersService.unlinkProducts(parseLong(id), productIds);
        return ResponseEntity.noContent().build();
    }

}