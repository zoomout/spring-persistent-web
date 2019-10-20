package com.bogdan.persistentweb.web;

import com.bogdan.persistentweb.domain.Product;
import com.bogdan.persistentweb.dto.ProductDto;
import com.bogdan.persistentweb.mapper.ProductMapper;
import com.bogdan.persistentweb.service.ProductsService;
import com.bogdan.persistentweb.validation.ValidLong;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.bogdan.persistentweb.utils.UriUtil.createUri;

@RestController
@RequestMapping("/products")
@Validated
public class ProductsController {

    private final ProductsService ProductsService;
    private final ProductMapper ProductMapper;

    public ProductsController(ProductsService ProductsService, ProductMapper ProductMapper) {
        this.ProductsService = ProductsService;
        this.ProductMapper = ProductMapper;
    }

    @GetMapping
    public List<ProductDto> getProducts() {
        return ProductsService
                .getProducts().stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<ProductDto> postProduct(@Valid @RequestBody ProductDto ProductDto) {
        final Product createdProduct = ProductsService.create(ProductMapper.toEntity(ProductDto));
        return ResponseEntity.created(createUri("/Products/" + createdProduct.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@ValidLong @PathVariable String id) {
        return ProductsService
                .get(Long.parseLong(id))
                .map(Product -> ResponseEntity.ok().body(ProductMapper.toDto(Product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> putProduct(@ValidLong @PathVariable String id,
                                                 @Valid @RequestBody ProductDto ProductDto) {
        return ProductsService
                .update(ProductMapper.toEntity(Long.parseLong(id), ProductDto))
                .map(Product -> ResponseEntity.ok().body(ProductMapper.toDto(Product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@ValidLong @PathVariable String id) {
        return ProductsService
                .delete(Long.parseLong(id))
                .map(ignored -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }

}