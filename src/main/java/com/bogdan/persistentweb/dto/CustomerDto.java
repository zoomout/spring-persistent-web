package com.bogdan.persistentweb.dto;

import com.bogdan.persistentweb.validation.LeadingOrTrailingSpacesNotAllowed;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class CustomerDto extends IdDto {

  @LeadingOrTrailingSpacesNotAllowed
  @NotNull(message = "should not be null")
  private final String name;

  @JsonCreator
  public CustomerDto(
      @JsonProperty("id") final String id,
      @JsonProperty("name") final String name
  ) {
    super(id);
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    CustomerDto that = (CustomerDto) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), name);
  }
}
