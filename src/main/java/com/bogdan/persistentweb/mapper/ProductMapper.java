package com.bogdan.persistentweb.mapper;

import com.bogdan.persistentweb.domain.Product;
import com.bogdan.persistentweb.dto.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto toDto(final Product Product) {
        if (Product == null) {
            return null;
        }
        return new ProductDto(String.valueOf(Product.getId()), Product.getTitle());
    }

    public Product toEntity(final ProductDto ProductDto) {
        return toEntity(null, ProductDto);
    }

    public Product toEntity(final Long id, final ProductDto ProductDto) {
        if (ProductDto == null) {
            return null;
        }
        Product Product = new Product();
        if (id != null) {
            Product.setId(id);
        }
        Product.setTitle(ProductDto.getTitle());
        return Product;
    }
}
