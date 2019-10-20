package com.bogdan.persistentweb.dto;

import com.bogdan.persistentweb.validation.LeadingOrTrailingSpacesNotAllowed;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Size;
import java.util.Objects;

public class IdDto {

    @LeadingOrTrailingSpacesNotAllowed
    @Size(max = 255, message = "size must be between 1 and 255")
    private final String id;

    @JsonCreator
    public IdDto(@JsonProperty("id") final String id
    ) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final IdDto idDto = (IdDto) o;
        return Objects.equals(id, idDto.id);
    }
}
