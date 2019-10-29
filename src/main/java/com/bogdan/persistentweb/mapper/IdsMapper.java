package com.bogdan.persistentweb.mapper;

import com.bogdan.persistentweb.dto.IdDto;
import com.bogdan.persistentweb.exception.InvalidPropertyException;

import java.util.Set;
import java.util.stream.Collectors;

public class IdsMapper {

  public static <T extends IdDto> Set<Long> toIdsSet(Set<T> productIds) {
    return productIds.stream().map(p -> parseLong(p.getId())).collect(Collectors.toSet());
  }

  private static Long parseLong(final String id) {
    try {
      return Long.valueOf(id);
    } catch (NumberFormatException e) {
      throw new InvalidPropertyException("id", id);
    }
  }
}