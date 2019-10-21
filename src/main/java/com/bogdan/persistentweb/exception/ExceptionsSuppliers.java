package com.bogdan.persistentweb.exception;

import java.util.function.Supplier;

public class ExceptionsSuppliers {

    public static Supplier<EntityNotFoundException> productNotFoundException(long id) {
        return () -> new EntityNotFoundException("product", String.valueOf(id));
    }

    public static Supplier<EntityNotFoundException> customerNotFoundException(long id) {
        return () -> new EntityNotFoundException("customer", String.valueOf(id));
    }
}
