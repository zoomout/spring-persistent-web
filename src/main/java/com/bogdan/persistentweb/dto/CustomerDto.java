package com.bogdan.persistentweb.dto;

import com.bogdan.persistentweb.validation.LeadingOrTrailingSpacesNotAllowed;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class CustomerDto {

    private final String id;

    @LeadingOrTrailingSpacesNotAllowed
    @NotNull(message = "should not be null")
    private final String name;

    @JsonCreator
    public CustomerDto(
            @JsonProperty("id") final String id,
            @JsonProperty("name") final String name
    ) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerDto customerDto = (CustomerDto) o;
        return Objects.equals(id, customerDto.id) &&
                Objects.equals(name, customerDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
