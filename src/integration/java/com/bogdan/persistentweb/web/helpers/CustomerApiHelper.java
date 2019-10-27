package com.bogdan.persistentweb.web.helpers;

import com.bogdan.persistentweb.utils.ApiClient;
import com.bogdan.persistentweb.web.dto.TestCustomer;
import org.springframework.test.web.servlet.MvcResult;

import static com.bogdan.persistentweb.utils.EntityGenerators.generateCustomerDto;
import static com.bogdan.persistentweb.utils.HeaderUtils.idFromLocationHeader;
import static com.bogdan.persistentweb.utils.SerializationUtils.deserialized;
import static com.bogdan.persistentweb.utils.SerializationUtils.serialized;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerApiHelper {

  private ApiClient client;
  public static final String CUSTOMERS_PATH = "/customers/";

  public CustomerApiHelper(final ApiClient client) {
    this.client = client;
  }

  public TestCustomer customerFrom(final MvcResult result) throws java.io.IOException {
    return deserialized(result.getResponse().getContentAsString(), TestCustomer.class);
  }

  public void updateCustomer(final TestCustomer customer) throws Exception {
    client.put(CUSTOMERS_PATH + customer.getId(), serialized(customer)).andExpect(status().isNoContent());
  }

  public TestCustomer createCustomer() throws Exception {
    final TestCustomer customer = generateCustomerDto();
    final MvcResult result = client.post(CUSTOMERS_PATH, serialized(customer)).andExpect(status().isCreated()).andReturn();
    customer.setId(idFromLocationHeader(CUSTOMERS_PATH, result));
    return customer;
  }
}
