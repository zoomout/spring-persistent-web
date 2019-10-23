package com.bogdan.persistentweb.dto;

import com.bogdan.persistentweb.validation.LeadingOrTrailingSpacesNotAllowed;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class ProductDto extends IdDto {

  @LeadingOrTrailingSpacesNotAllowed
  @NotNull(message = "should not be null")
  private final String title;

  @JsonCreator
  public ProductDto(
      @JsonProperty("id") final String id,
      @JsonProperty("title") final String title
  ) {
    super(id);
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    ProductDto that = (ProductDto) o;
    return Objects.equals(title, that.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), title);
  }
}
