package com.bogdan.persistentweb.web.helpers;

import com.bogdan.persistentweb.utils.ApiClient;
import com.bogdan.persistentweb.web.dto.TestProduct;
import org.springframework.test.web.servlet.MvcResult;

import static com.bogdan.persistentweb.utils.EntityGenerators.generateProductDto;
import static com.bogdan.persistentweb.utils.HeaderUtils.idFromLocationHeader;
import static com.bogdan.persistentweb.utils.SerializationUtils.deserialized;
import static com.bogdan.persistentweb.utils.SerializationUtils.serialized;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductApiHelper {

  private ApiClient client;
  public static final String CUSTOMERS_PATH = "/products/";

  public ProductApiHelper(final ApiClient client) {
    this.client = client;
  }

  public TestProduct productFrom(final MvcResult result) throws java.io.IOException {
    return deserialized(result.getResponse().getContentAsString(), TestProduct.class);
  }

  public void updateProduct(final TestProduct product) throws Exception {
    client.put(CUSTOMERS_PATH + product.getId(), serialized(product)).andExpect(status().isNoContent());
  }

  public TestProduct createProduct() throws Exception {
    final TestProduct product = generateProductDto();
    final MvcResult result = client.post(CUSTOMERS_PATH, serialized(product)).andExpect(status().isCreated()).andReturn();
    product.setId(idFromLocationHeader(CUSTOMERS_PATH, result));
    return product;
  }
}
