package com.bogdan.persistentweb.web;

import com.bogdan.persistentweb.domain.Product;
import com.bogdan.persistentweb.dto.CustomerDto;
import com.bogdan.persistentweb.dto.ProductDto;
import com.bogdan.persistentweb.mapper.CustomerMapper;
import com.bogdan.persistentweb.mapper.ProductMapper;
import com.bogdan.persistentweb.service.ProductsService;
import com.bogdan.persistentweb.validation.ValidLong;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bogdan.persistentweb.mapper.IdsMapper.toIdsSet;
import static com.bogdan.persistentweb.utils.UriUtil.createUri;
import static java.lang.Long.parseLong;

@RestController
@RequestMapping("/products")
@Validated
public class ProductsController {

    private final ProductsService productsService;

    public ProductsController(
            ProductsService productsService
    ) {
        this.productsService = productsService;
    }

    @GetMapping
    public List<ProductDto> getProducts() {
        return productsService
                .getProducts().stream()
                .sorted()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<ProductDto> postProduct(@Valid @RequestBody ProductDto productDto) {
        final Product createdProduct = productsService.create(ProductMapper.toEntity(productDto));
        return ResponseEntity.created(createUri("/products/" + createdProduct.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@ValidLong @PathVariable String id) {
        return ResponseEntity.ok().body(ProductMapper.toDto(productsService.get(parseLong(id))));
    }

    @PutMapping("/{id}")
    public ResponseEntity putProduct(@ValidLong @PathVariable String id,
                                     @Valid @RequestBody ProductDto productDto) {
        productsService.update(ProductMapper.toEntity(parseLong(id), productDto));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@ValidLong @PathVariable String id) {
        productsService.delete(parseLong(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/customers")
    public ResponseEntity<Set<CustomerDto>> getProductCustomers(@ValidLong @PathVariable String id) {
        return ResponseEntity.ok().body(
                productsService.get(parseLong(id)).getCustomers().stream()
                        .sorted()
                        .map(CustomerMapper::toDto)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

    @PutMapping("/{id}/customers")
    public ResponseEntity putProductCustomers(
            @ValidLong @PathVariable String id,
            @RequestBody Set<CustomerDto> customersIds
    ) {
        productsService.addCustomers(parseLong(id), toIdsSet(customersIds));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/customers")
    public ResponseEntity deleteProductCustomers(
            @ValidLong @PathVariable String id,
            @RequestBody Set<CustomerDto> customersIds
    ) {
        productsService.removeCustomers(parseLong(id), toIdsSet(customersIds));
        return ResponseEntity.noContent().build();
    }

}