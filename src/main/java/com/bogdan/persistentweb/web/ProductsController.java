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

import static com.bogdan.persistentweb.utils.UriUtil.createUri;
import static java.lang.Long.parseLong;

@RestController
@RequestMapping("/products")
@Validated
public class ProductsController {

    private final ProductsService productsService;
    private final ProductMapper productMapper;
    private final CustomerMapper customerMapper;

    public ProductsController(
            ProductsService productsService,
            ProductMapper productMapper,
            CustomerMapper customerMapper
    ) {
        this.productsService = productsService;
        this.productMapper = productMapper;
        this.customerMapper = customerMapper;
    }

    @GetMapping
    public List<ProductDto> getProducts() {
        return productsService
                .getProducts().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<ProductDto> postProduct(@Valid @RequestBody ProductDto productDto) {
        final Product createdProduct = productsService.create(productMapper.toEntity(productDto));
        return ResponseEntity.created(createUri("/products/" + createdProduct.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@ValidLong @PathVariable String id) {
        return ResponseEntity.ok().body(productMapper.toDto(productsService.get(parseLong(id))));
    }

    @PutMapping("/{id}")
    public ResponseEntity putProduct(@ValidLong @PathVariable String id,
                                     @Valid @RequestBody ProductDto productDto) {
        productsService.update(productMapper.toEntity(parseLong(id), productDto));
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
                        .map(customerMapper::toDto)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

    @PutMapping("/{id}/customers")
    public ResponseEntity putProductProducts(
            @ValidLong @PathVariable String id,
            @RequestBody Set<Long> productIds
    ) {
        productsService.linkCustomers(parseLong(id), productIds);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/products")
    public ResponseEntity deleteProductProducts(
            @ValidLong @PathVariable String id,
            @RequestBody Set<Long> productIds
    ) {
        productsService.unlinkCustomers(parseLong(id), productIds);
        return ResponseEntity.noContent().build();
    }

}