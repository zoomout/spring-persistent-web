package com.bogdan.persistentweb.web;

import com.bogdan.persistentweb.utils.ApiClient;
import com.bogdan.persistentweb.web.dto.TestProduct;
import com.bogdan.persistentweb.web.helpers.ProductApiHelper;
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
import static com.bogdan.persistentweb.utils.EntityGenerators.generateProductDto;
import static com.bogdan.persistentweb.utils.GenericTestData.invalidPayloadData;
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
class ProductsControllerTest {

  @Autowired
  private MockMvc mockMvc;
  private ApiClient client;
  private ProductApiHelper productApi;

  private static final String PRODUCTS_PATH = "/products/";
  private static final String CUSTOMERS_PATH = "/customers";

  @BeforeEach
  void beforeAll() {
    client = new ApiClient(mockMvc);
    productApi = new ProductApiHelper(client);
  }

  private static Stream<Arguments> validProducts() {
    return Stream.of(
        Arguments.of(generateProductDto()),
        Arguments.of(generateProductDto().setId("shouldBeIgnored"))
    );
  }

  @ParameterizedTest
  @MethodSource("validProducts")
  void callingPost_shouldCreateProduct(final TestProduct product) throws Exception {
    // When create content with a valid product payload
    final ResultActions result = client.post(PRODUCTS_PATH, serialized(product));
    // Then status code is '201 - Created' AND Location header has numeric product Id
    result
        .andExpect(status().isCreated())
        .andDo((r) -> assertThat(valueOf(idFromLocationHeader(PRODUCTS_PATH, r)), greaterThan(0)));
  }

  static Stream<Arguments> invalidPayload() {
    return invalidPayloadData();
  }

  @ParameterizedTest
  @MethodSource("invalidPayload")
  void callingPost_withInvalidContent_shouldReturn400(final String payload) throws Exception {
    // When create a product with invalid product payload
    final ResultActions result = client.post(PRODUCTS_PATH, payload);

    // Then expect response 400 - Bad request
    result.andExpect(status().isBadRequest());
  }

  @Test
  void callingPost_withoutRequiredField_shouldReturn400() throws Exception {
    // When create a product with invalid payload
    final ResultActions result = client.post("/products", "{\"foo\":\"bar\"}");

    // Then expect response 400 - Bad request
    result.andExpect(status().isBadRequest());
    result.andExpect(content().string("{\"message\":\"validation of field 'title' failed: should not be null\"}"));
  }

  @Test
  void callingPut_shouldUpdateProduct() throws Exception {
    // Given created product
    final TestProduct createdProduct = productApi.createProduct();

    // When update content with a valid product payload
    final TestProduct updatedProduct = generateProductDto().setId(createdProduct.getId()).setTitle("newTitle");
    final ResultActions result = client.put(PRODUCTS_PATH + updatedProduct.getId(), serialized(updatedProduct));

    // Then status code is '204 - no content'
    result.andExpect(status().isNoContent());
    // And product is updated
    client.get(PRODUCTS_PATH, createdProduct.getId())
        .andExpect(status().isOk())
        .andDo((response) -> assertThat(productApi.productFrom(response), is(updatedProduct)));
  }

  @Test
  void callingGet_shouldRetrieveProduct() throws Exception {
    // Given product is created
    final TestProduct createdProduct = productApi.createProduct();

    // When get the product
    final ResultActions resultActions = client.get(PRODUCTS_PATH, createdProduct.getId());

    // Then response is 200 - OK and retrieved product is the same as the created one
    resultActions
        .andExpect(status().isOk())
        .andDo((response) -> assertThat(productApi.productFrom(response), is(createdProduct)));
  }

  @Test
  void callingDelete_shouldDeleteAProduct() throws Exception {
    // Given product is created
    final TestProduct createdProduct = productApi.createProduct();

    // When delete the product
    final ResultActions deleteResult = client.delete(PRODUCTS_PATH, createdProduct.getId());

    // Then response is '204 - No content'
    deleteResult.andExpect(status().isNoContent());
    // And '404 - Non found' is returned when trying to retrieve the product
    client.get(PRODUCTS_PATH, createdProduct.getId()).andExpect(status().isNotFound());
  }

  @Test
  void callingGetAll_shouldReturnAList() throws Exception {
    // When get all products is called
    final ResultActions result = client.getAll(PRODUCTS_PATH);

    // Then response is '200 - OK' and paginated result in payload
    result.andExpect(status().isOk());
    assertPaginationResult(result);
  }

  @Test
  void callingGetProductCustomers_forProductWithoutCustomers_shouldReturnEmptyList() throws Exception {
    // Given product is created
    final TestProduct createdProduct = productApi.createProduct();

    // When get all product's customers is called
    final ResultActions result = client.getAll(PRODUCTS_PATH + createdProduct.getId() + CUSTOMERS_PATH);

    // Then response is '200 - OK' and empty array in payload
    result
        .andExpect(status().isOk())
        .andExpect(content().string("[]"));
  }

}
