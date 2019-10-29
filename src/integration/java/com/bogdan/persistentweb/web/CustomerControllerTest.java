package com.bogdan.persistentweb.web;

import com.bogdan.persistentweb.utils.ApiClient;
import com.bogdan.persistentweb.web.dto.TestCustomer;
import com.bogdan.persistentweb.web.helpers.CustomerApiHelper;
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
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static com.bogdan.persistentweb.utils.AssertionUtils.assertPaginationResult;
import static com.bogdan.persistentweb.utils.EntityGenerators.generateCustomerDto;
import static com.bogdan.persistentweb.utils.GenericTestData.invalidPayloadData;
import static com.bogdan.persistentweb.utils.GenericTestData.leadingAndTrailingSpaceData;
import static com.bogdan.persistentweb.utils.HeaderUtils.idFromLocationHeader;
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
  private CustomerApiHelper customerApi;

  private static final String CUSTOMERS_PATH = "/customers/";
  private static final String PRODUCTS_PATH = "/products";

  @BeforeEach
  void beforeAll() {
    client = new ApiClient(mockMvc);
    customerApi = new CustomerApiHelper(client);
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
  void callingPost_withUnknownField_shouldReturn400() throws Exception {
    // When create a customer with invalid customer payload
    final ResultActions result = client.post("/customers", "{\"foo\":\"bar\"}");

    // Then expect response 400 - Bad request
    result.andExpect(status().isBadRequest());
    result.andExpect(content().string(
        "{\"message\":\"validation of field 'name' failed: should not be null\"}"));
  }

  @Test
  void callingPut_shouldUpdateCustomer() throws Exception {
    // Given created customer
    final TestCustomer createdCustomer = customerApi.createCustomer();

    // When update content with a valid customer payload
    final TestCustomer updatedCustomer = generateCustomerDto().setId(createdCustomer.getId()).setName("newName");
    final ResultActions result = client.put(CUSTOMERS_PATH + updatedCustomer.getId(), serialized(updatedCustomer));

    // Then status code is '204 - no content'
    result.andExpect(status().isNoContent());
    // And customer is updated
    client.get(CUSTOMERS_PATH, createdCustomer.getId())
        .andExpect(status().isOk())
        .andDo((response) -> assertThat(customerApi.customerFrom(response), is(updatedCustomer)));
  }

  static Stream<Arguments> leadingAndTrailingSpace() {
    return leadingAndTrailingSpaceData();
  }

  @ParameterizedTest
  @MethodSource("leadingAndTrailingSpace")
  void callingPost_withLeadingSpace_shouldReturn400(final String value) throws Exception {
    // When create a customer with invalid customer payload
    final ResultActions result = client.post("/customers", "{\"name\":\"" + value + "\"}");

    // Then expect response 400 - Bad request
    result.andExpect(status().isBadRequest());
    result.andExpect(content().string(
        "{\"message\":\"validation of field 'name' failed: leading or trailing spaces are not allowed\"}"));
  }

  @Test
  void callingGet_shouldRetrieveCustomer() throws Exception {
    // Given customer is created
    final TestCustomer createdCustomer = customerApi.createCustomer();

    // When get the customer
    final ResultActions resultActions = client.get(CUSTOMERS_PATH, createdCustomer.getId());

    // Then response is 200 - OK and retrieved customer is the same as the created one
    resultActions
        .andExpect(status().isOk())
        .andDo((response) -> assertThat(customerApi.customerFrom(response), is(createdCustomer)));
  }

  @Test
  void callingDelete_shouldDeleteACustomer() throws Exception {
    // Given customer is created
    final TestCustomer createdCustomer = customerApi.createCustomer();

    // When delete the customer
    final ResultActions deleteResult = client.delete(CUSTOMERS_PATH, createdCustomer.getId());

    // Then response is '204 - No content'
    deleteResult.andExpect(status().isNoContent());
    // And '404 - Non found' is returned when trying to retrieve the customer
    client.get(CUSTOMERS_PATH, createdCustomer.getId()).andExpect(status().isNotFound());
  }

  @Test
  void callingGetAll_shouldReturnAList() throws Exception {
    // When get all customers
    final ResultActions result = client.getAll(CUSTOMERS_PATH);

    // Then response is '200 - OK' and paginated result in payload
    result.andExpect(status().isOk());
    assertPaginationResult(result);
  }

  @Test
  void callingGetCustomerProducts_forCustomerWithoutProducts_shouldReturnEmptyList() throws Exception {
    // Given customer is created
    final TestCustomer createdCustomer = customerApi.createCustomer();

    // When get all customer's products is called
    final ResultActions result = client.getAll(CUSTOMERS_PATH + createdCustomer.getId() + PRODUCTS_PATH);

    // Then response is '200 - OK' and empty array in payload
    result
        .andExpect(status().isOk())
        .andExpect(content().string("[]"));
  }

  @Test
  void getCustomer_withStringId_shouldReturnError() throws Exception {
    final String stringId = "s";
    ResultActions result = client.get(CUSTOMERS_PATH, stringId);
    String expectedError =
        "{\"message\":\"constraint is violated for path parameter 'id': should be a valid long number\"}";
    result
        .andExpect(status().isBadRequest())
        .andExpect(content().string(expectedError));
  }

}
