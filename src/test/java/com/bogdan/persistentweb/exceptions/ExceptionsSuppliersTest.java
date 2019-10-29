package com.bogdan.persistentweb.exceptions;

import com.bogdan.persistentweb.exception.EntityNotFoundException;
import com.bogdan.persistentweb.exception.ExceptionsSuppliers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

class ExceptionsSuppliersTest {

  @Test
  void testCustomerNotFoundExceptionIsSupplied() {
    final EntityNotFoundException exception = ExceptionsSuppliers.customerNotFoundException(1).get();
    assertThat(exception.getMessage(), is("Entity 'customer' with id '1' not found"));
  }

  @Test
  void testProductNotFoundExceptionIsSupplied() {
    final EntityNotFoundException exception = ExceptionsSuppliers.productNotFoundException(2).get();
    assertThat(exception.getMessage(), is("Entity 'product' with id '2' not found"));
  }

}
