package com.bogdan.persistentweb.web;

import com.bogdan.persistentweb.utils.ApiClient;
import com.bogdan.persistentweb.web.dto.TestCustomer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static com.bogdan.persistentweb.utils.EntityGenerators.generateCustomerDto;
import static com.bogdan.persistentweb.utils.GenericTestData.invalidPayloadData;
import static com.bogdan.persistentweb.utils.HeaderUtils.idFromLocationHeader;
import static com.bogdan.persistentweb.utils.MatcherUtils.jsonArrayMatcher;
import static com.bogdan.persistentweb.utils.SerializationUtils.deserialized;
import static com.bogdan.persistentweb.utils.SerializationUtils.serialized;
import static java.lang.Integer.valueOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

  @Autowired
  private MockMvc mockMvc;
  private ApiClient client;

  private static final String CUSTOMERS_PATH = "/customers/";
  private static final String PRODUCTS_PATH = "/products";

  @BeforeEach
  void beforeAll() {
    client = new ApiClient(mockMvc);
  }

  private static Stream<Arguments> validCustomers() {
    return Stream.of(
        Arguments.of(generateCustomerDto()),
        Arguments.of(generateCustomerDto().setId("shouldBeIgnored"))
    );
  }

  @ParameterizedTest
  @MethodSource("validCustomers")
  void callingPost_shouldCreateCustomer(final TestCustomer customer) throws Exception {
    // When create content with a valid customer payload
    ResultActions result = client.post(CUSTOMERS_PATH, serialized(customer));

    // Then status code is '201 - Created' AND Location header has numeric customer Id
    result
        .andExpect(status().isCreated())
        .andDo((r) -> assertThat(valueOf(idFromLocationHeader(CUSTOMERS_PATH, r)), greaterThan(0)));
  }

  static Stream<Arguments> invalidPayload() {
    return invalidPayloadData();
  }

  @ParameterizedTest
  @MethodSource("invalidPayload")
  void callingPost_withInvalidContent_shouldReturn400(final String payload) throws Exception {
    // When create a customer with invalid customer payload
    final ResultActions result = client.post(CUSTOMERS_PATH, payload);
    // Then expect response 400 - Bad request
    result.andExpect(status().isBadRequest());
  }

  @Test
  void callingGet_shouldRetrieveCustomer() throws Exception {
    // Given customer is created
    final TestCustomer createdCustomer = createCustomer();

    // When get the customer
    final ResultActions resultActions = client.get(CUSTOMERS_PATH, createdCustomer.getId());

    // Then response is 200 - OK and retrieved customer is the same as the created one
    resultActions
        .andExpect(status().isOk())
        .andDo((response) -> assertThat(customerFrom(response), is(createdCustomer)));
  }

  @Test
  void callingDelete_shouldDeleteACustomer() throws Exception {
    // Given customer is created
    final TestCustomer createdCustomer = createCustomer();

    // When delete the customer
    final ResultActions deleteResult = client.delete(CUSTOMERS_PATH, createdCustomer.getId());

    // Then response is '204 - No content'
    deleteResult.andExpect(status().isNoContent());
    // And '404 - Non found' is returned when trying to retrieve the content
    client.get(CUSTOMERS_PATH, createdCustomer.getId()).andExpect(status().isNotFound());
  }

  @Test
  void callingGetAll_shouldReturnAList() throws Exception {
    // When get all customers products is called
    final ResultActions result = client.getAll(CUSTOMERS_PATH);

    // Then response is '200 - OK' and empty array in payload
    result
        .andExpect(status().isOk())
        .andExpect(content().string(jsonArrayMatcher()));
  }

  @Test
  void callingGetCustomerProducts_forCustomerWithoutProducts_shouldReturnEmptyList() throws Exception {
    // Given customer is created
    final TestCustomer createdCustomer = createCustomer();

    // When get all customers is called
    final ResultActions result = client.getAll(CUSTOMERS_PATH + createdCustomer.getId() + PRODUCTS_PATH);

    // Then response is '200 - OK' and empty array in payload
    result
        .andExpect(status().isOk())
        .andExpect(content().string("[]"));
  }

  private TestCustomer customerFrom(final MvcResult result) throws java.io.IOException {
    return deserialized(result.getResponse().getContentAsString(), TestCustomer.class);
  }

  private TestCustomer createCustomer() throws Exception {
    final TestCustomer customer = generateCustomerDto();
    final MvcResult result = client.post(CUSTOMERS_PATH, serialized(customer)).andExpect(status().isCreated()).andReturn();
    customer.setId(idFromLocationHeader(CUSTOMERS_PATH, result));
    return customer;
  }

}
