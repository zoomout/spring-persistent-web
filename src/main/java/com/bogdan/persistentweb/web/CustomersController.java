package com.bogdan.persistentweb.web;

import com.bogdan.persistentweb.domain.Customer;
import com.bogdan.persistentweb.dto.CustomerDto;
import com.bogdan.persistentweb.dto.PageDto;
import com.bogdan.persistentweb.dto.ProductDto;
import com.bogdan.persistentweb.mapper.CustomerMapper;
import com.bogdan.persistentweb.mapper.ProductMapper;
import com.bogdan.persistentweb.service.CustomersService;
import com.bogdan.persistentweb.validation.ValidLong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bogdan.persistentweb.mapper.CustomerMapper.toDto;
import static com.bogdan.persistentweb.mapper.IdsMapper.toIdsSet;
import static com.bogdan.persistentweb.utils.UriUtil.createUri;
import static java.lang.Long.parseLong;

@RestController
@RequestMapping("/customers")
@Validated
public class CustomersController {

  private final CustomersService customersService;

  public CustomersController(
      CustomersService customersService
  ) {
    this.customersService = customersService;
  }

  @GetMapping
  public PageDto<CustomerDto> getCustomers(final Pageable pageable) {
    final Page<Customer> customersPage = customersService.getCustomers(pageable);
    return new PageDto<>(
        customersPage.getTotalPages(),
        customersPage.stream()
            .map(CustomerMapper::toDto)
            .collect(Collectors.toList())
    );
  }

  @PostMapping
  public ResponseEntity<CustomerDto> postCustomer(@Valid @RequestBody CustomerDto customerDto) {
    final Customer createdCustomer = customersService.create(CustomerMapper.toEntity(customerDto));
    return ResponseEntity.created(createUri("/customers/" + createdCustomer.getId())).build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<CustomerDto> getCustomer(@ValidLong @PathVariable String id) {
    return ResponseEntity.ok().body(toDto(customersService.get(parseLong(id))));
  }

  @PutMapping("/{id}")
  public ResponseEntity putCustomer(@ValidLong @PathVariable String id,
                                    @Valid @RequestBody CustomerDto customerDto) {
    customersService.update(CustomerMapper.toEntity(parseLong(id), customerDto));
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
            .map(ProductMapper::toDto)
            .collect(Collectors.toCollection(LinkedHashSet::new)
            ));
  }

  @PutMapping("/{id}/products")
  public ResponseEntity putCustomerProducts(
      @ValidLong @PathVariable String id,
      @RequestBody Set<ProductDto> productIds
  ) {
    customersService.addProducts(parseLong(id), toIdsSet(productIds));
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}/products")
  public ResponseEntity deleteCustomerProducts(
      @ValidLong @PathVariable String id,
      @RequestBody Set<ProductDto> productIds
  ) {
    customersService.removeProducts(parseLong(id), toIdsSet(productIds));
    return ResponseEntity.noContent().build();
  }

}