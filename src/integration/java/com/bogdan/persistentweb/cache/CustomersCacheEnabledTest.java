package com.bogdan.persistentweb.cache;

import com.bogdan.persistentweb.utils.ApiClient;
import com.bogdan.persistentweb.web.dto.TestCustomer;
import com.bogdan.persistentweb.web.helpers.CustomerApiHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.bogdan.persistentweb.web.helpers.CustomerApiHelper.CUSTOMERS_PATH;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"cache.enabled=true"})
@AutoConfigureMockMvc
class CustomersCacheEnabledTest {

  @Autowired
  private MockMvc mockMvc;
  private ApiClient client;
  private CustomerApiHelper customerApi;

  @BeforeEach
  void beforeAll() {
    client = new ApiClient(mockMvc);
    customerApi = new CustomerApiHelper(client);
  }

  @Test
  void shouldGetOriginalCustomerAfterUpdate_whenCacheIsEnabled() throws Exception {
    // Given customer is created
    final TestCustomer createdCustomer = customerApi.createCustomer();
    final String customerId = createdCustomer.getId();

    // Ans get customer is called
    client.get(CUSTOMERS_PATH, customerId);

    // When update customer
    final TestCustomer updatedCustomer = new TestCustomer().setId(customerId).setName("UpdatedName");
    customerApi.updateCustomer(updatedCustomer);

    // And call get again
    final ResultActions resultActions = client.get(CUSTOMERS_PATH, customerId);

    // Then the response has the originally created customer because cache is enabled
    resultActions
        .andExpect(status().isOk())
        .andDo((response) -> assertThat(customerApi.customerFrom(response), is(createdCustomer)));
  }

}
