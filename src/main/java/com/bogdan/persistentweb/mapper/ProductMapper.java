package com.bogdan.persistentweb.mapper;

import com.bogdan.persistentweb.domain.Product;
import com.bogdan.persistentweb.dto.ProductDto;

public class ProductMapper {

  public static ProductDto toDto(final Product Product) {
    return new ProductDto(String.valueOf(Product.getId()), Product.getTitle());
  }

  public static Product toEntity(final ProductDto ProductDto) {
    return toEntity(null, ProductDto);
  }

  public static Product toEntity(final Long id, final ProductDto ProductDto) {
    Product Product = new Product();
    if (id != null) {
      Product.setId(id);
    }
    Product.setTitle(ProductDto.getTitle());
    return Product;
  }
}
