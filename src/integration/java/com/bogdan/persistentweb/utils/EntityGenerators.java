package com.bogdan.persistentweb.utils;

import com.bogdan.persistentweb.web.dto.TestCustomer;
import com.bogdan.persistentweb.web.dto.TestProduct;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public class EntityGenerators {

  public static TestCustomer generateCustomerDto() {
    return new TestCustomer().setName("customer_" + randomAlphanumeric(6));
  }

  public static TestProduct generateProductDto() {
    return new TestProduct().setTitle("product_" + randomAlphanumeric(6));
  }
}
