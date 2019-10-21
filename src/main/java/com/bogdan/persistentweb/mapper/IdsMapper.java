package com.bogdan.persistentweb.mapper;

import com.bogdan.persistentweb.dto.IdDto;

import java.util.Set;
import java.util.stream.Collectors;

public class IdsMapper {
    public static <T extends IdDto> Set<Long> toIdsSet(Set<T> productIds) {
        return productIds.stream().map(p -> Long.valueOf(p.getId())).collect(Collectors.toSet());
    }
}