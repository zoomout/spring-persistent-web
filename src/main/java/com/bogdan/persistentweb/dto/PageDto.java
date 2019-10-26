package com.bogdan.persistentweb.dto;

import java.util.List;

public class PageDto<T extends IdDto> {

  private Integer totalPages;
  private List<T> items;

  public PageDto(final Integer totalPages, final List<T> items) {
    this.totalPages = totalPages;
    this.items = items;
  }

  public Integer getTotalPages() {
    return totalPages;
  }

  public List<T> getItems() {
    return items;
  }
}
