package com.bogdan.persistentweb.web.dto;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GetAllDto {

  private Long totalPages;
  private List<Map<String, String>> items;

  public Long getTotalPages() {
    return totalPages;
  }

  public GetAllDto setTotalPages(final Long totalPages) {
    this.totalPages = totalPages;
    return this;
  }

  public List<Map<String, String>> getItems() {
    return items;
  }

  public GetAllDto setItems(final List<Map<String, String>> items) {
    this.items = items;
    return this;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final GetAllDto getAllDto = (GetAllDto) o;
    return Objects.equals(totalPages, getAllDto.totalPages) &&
        Objects.equals(items, getAllDto.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalPages, items);
  }

  @Override
  public String toString() {
    return "GetAllDto{" +
        "totalPages=" + totalPages +
        ", items=" + items +
        '}';
  }
}
