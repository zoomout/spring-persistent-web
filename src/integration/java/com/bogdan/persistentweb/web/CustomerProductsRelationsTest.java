package com.bogdan.persistentweb.web;

import com.bogdan.persistentweb.utils.ApiClient;
import com.bogdan.persistentweb.web.dto.TestCustomer;
import com.bogdan.persistentweb.web.dto.TestProduct;
import com.bogdan.persistentweb.web.helpers.CustomerApiHelper;
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

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Stream;

import static com.bogdan.persistentweb.utils.SerializationUtils.deserializedList;
import static com.bogdan.persistentweb.utils.SerializationUtils.serialized;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CustomerProductsRelationsTest {

  @Autowired
  private MockMvc mockMvc;
  private ApiClient client;
  private CustomerApiHelper customerApi;
  private ProductApiHelper productApi;

  private static final String CUSTOMERS_PATH = "/customers/";
  private static final String PRODUCTS_PATH = "/products/";

  @BeforeEach
  void beforeAll() {
    client = new ApiClient(mockMvc);
    customerApi = new CustomerApiHelper(client);
    productApi = new ProductApiHelper(client);
  }

  public static Stream<Arguments> bidirectionalRelations() {
    return Stream.of(
        Arguments.of(CreateRelationType.PRODUCT_TO_CUSTOMER),
        Arguments.of(CreateRelationType.CUSTOMER_TO_PRODUCT)
    );
  }

  @ParameterizedTest
  @MethodSource("bidirectionalRelations")
  void creationRelationBetweenCustomerAndProduct_shouldBeSuccessful(
      final CreateRelationType createRelationType
  ) throws Exception {
    // Given customer and product are created
    final TestCustomer customer = customerApi.createCustomer();
    final TestProduct product = productApi.createProduct();

    // When relation between customer and product is created
    ResultActions result = createRelationship(createRelationType, customer, product);

    // Then response is '204 - no content'
    result.andExpect(status().isNoContent());
  }

  @ParameterizedTest
  @MethodSource("bidirectionalRelations")
  void retrievalOfBiDirectionalRelations_shouldBeSuccessful(
      final CreateRelationType createRelationType
  ) throws Exception {
    // Given customer and product
    final TestCustomer customer = customerApi.createCustomer();
    final TestProduct product = productApi.createProduct();

    // When relation between customer and product is created
    createRelationship(createRelationType, customer, product);

    // And retrieve products of customer
    final ResultActions getProductsResult = getProductsOfCustomer(customer);
    // And retrieve customers of products
    final ResultActions getCustomersResult = getCustomersOfProduct(product);

    // Then response is '200 - ok'
    getProductsResult.andExpect(status().isOk());
    getCustomersResult.andExpect(status().isOk());

    // And customer has expected product
    List<TestProduct> products = getListOfItems(getProductsResult, TestProduct.class);
    assertThat(products.size(), is(1));
    assertThat(products.get(0), is(product));

    // And product has expected customer
    List<TestCustomer> customers = getListOfItems(getCustomersResult, TestCustomer.class);
    assertThat(customers.size(), is(1));
    assertThat(customers.get(0), is(customer));
  }

  public static Stream<Arguments> breakRelationshipTypes() {
    return Stream.of(
        Arguments.of(CreateRelationType.PRODUCT_TO_CUSTOMER, BreakRelationType.PRODUCT_FROM_CUSTOMER),
        Arguments.of(CreateRelationType.PRODUCT_TO_CUSTOMER, BreakRelationType.CUSTOMER_FROM_PRODUCT),
        Arguments.of(CreateRelationType.CUSTOMER_TO_PRODUCT, BreakRelationType.PRODUCT_FROM_CUSTOMER),
        Arguments.of(CreateRelationType.CUSTOMER_TO_PRODUCT, BreakRelationType.CUSTOMER_FROM_PRODUCT)
    );
  }

  @ParameterizedTest
  @MethodSource("breakRelationshipTypes")
  void deletionOfBiDirectionalRelations_shouldBeSuccessful(
      final CreateRelationType createRelationType,
      final BreakRelationType breakRelationType
  ) throws Exception {
    // Given customer and product
    final TestCustomer customer = customerApi.createCustomer();
    final TestProduct product = productApi.createProduct();
    // And relationship between customer and product is created
    createRelationship(createRelationType, customer, product);

    // When break relationship
    ResultActions result = breakRelationship(breakRelationType, customer, product);
    // Then expect status '204 - no content'
    result.andExpect(status().isNoContent());

    // When retrieve products of customer and customers of product
    final ResultActions getProductsResult = getProductsOfCustomer(customer);
    final ResultActions getCustomersResult = getCustomersOfProduct(product);

    // Then response is '200 - ok'
    getProductsResult.andExpect(status().isOk());
    getCustomersResult.andExpect(status().isOk());
    // And customer has no products
    List<TestProduct> products = getListOfItems(getProductsResult, TestProduct.class);
    assertThat(products.size(), is(0));
    // And product has no customers
    List<TestCustomer> customers = getListOfItems(getCustomersResult, TestCustomer.class);
    assertThat(customers.size(), is(0));
  }

  @Test
  void callingDelete_forCustomerWithProducts_shouldDeleteACustomer() throws Exception {
    // Given customer and product are created
    final TestCustomer customer = customerApi.createCustomer();
    final TestProduct product = productApi.createProduct();
    // And product is added to customer
    createRelationship(CreateRelationType.PRODUCT_TO_CUSTOMER, customer, product);

    // When delete the customer
    final ResultActions deleteResult = client.delete(CUSTOMERS_PATH, customer.getId());

    // Then response is '204 - No content'
    deleteResult.andExpect(status().isNoContent());
    // And '404 - Non found' is returned when trying to retrieve the customer
    client.get(CUSTOMERS_PATH, customer.getId()).andExpect(status().isNotFound());
  }

  @Test
  void callingDelete_forProductWithCustomers_shouldDeleteAProduct() throws Exception {
    // Given customer and product are created
    final TestCustomer customer = customerApi.createCustomer();
    final TestProduct product = productApi.createProduct();
    // And customer is added to product
    createRelationship(CreateRelationType.CUSTOMER_TO_PRODUCT, customer, product);

    // When delete the product
    final ResultActions deleteResult = client.delete(PRODUCTS_PATH, product.getId());

    // Then response is '204 - No content'
    deleteResult.andExpect(status().isNoContent());
    // And '404 - Non found' is returned when trying to retrieve the product
    client.get(PRODUCTS_PATH, product.getId()).andExpect(status().isNotFound());
  }

  private ResultActions breakRelationship(
      final BreakRelationType breakRelationType,
      final TestCustomer customer,
      final TestProduct product
  ) {
    switch (breakRelationType) {
      case CUSTOMER_FROM_PRODUCT:
        return deleteCustomersFromProduct(product, List.of(customer));
      case PRODUCT_FROM_CUSTOMER:
        return deleteProductsFromCustomer(customer, List.of(product));
      default:
        throw new IllegalStateException("Unexpected value: " + breakRelationType);
    }
  }

  private ResultActions createRelationship(
      final CreateRelationType createRelationType,
      final TestCustomer customer,
      final TestProduct product
  ) {
    switch (createRelationType) {
      case CUSTOMER_TO_PRODUCT:
        return addCustomersToProduct(product, List.of(customer));
      case PRODUCT_TO_CUSTOMER:
        return addProductsToCustomer(customer, List.of(product));
      default:
        throw new IllegalStateException("Unexpected value: " + createRelationType);
    }
  }

  private <T> List<T> getListOfItems(final ResultActions result, final Class<T> clazz) throws UnsupportedEncodingException {
    return deserializedList(result.andReturn().getResponse().getContentAsString(), clazz);
  }

  private ResultActions getCustomersOfProduct(final TestProduct product) throws Exception {
    return client.getAll(PRODUCTS_PATH + product.getId() + CUSTOMERS_PATH);
  }

  private ResultActions getProductsOfCustomer(final TestCustomer customer) throws Exception {
    return client.getAll(CUSTOMERS_PATH + customer.getId() + PRODUCTS_PATH);
  }

  private ResultActions addProductsToCustomer(
      final TestCustomer customer,
      final List<TestProduct> products
  ) {
    try {
      return client.put(
          CUSTOMERS_PATH + customer.getId() + PRODUCTS_PATH,
          serialized(products)
      );
    } catch (Exception e) {
      throw new RuntimeException("Exception during adding products to customer", e);
    }
  }

  private ResultActions deleteProductsFromCustomer(
      final TestCustomer customer,
      final List<TestProduct> products
  ) {
    try {
      return client.deleteSubresources(
          CUSTOMERS_PATH + customer.getId() + PRODUCTS_PATH,
          serialized(products)
      );
    } catch (Exception e) {
      throw new RuntimeException("Exception during deletion of products from customer", e);
    }
  }

  private ResultActions addCustomersToProduct(
      final TestProduct product,
      final List<TestCustomer> customers
  ) {
    try {
      return client.put(
          PRODUCTS_PATH + product.getId() + CUSTOMERS_PATH,
          serialized(customers)
      );
    } catch (Exception e) {
      throw new RuntimeException("Exception during adding customers to product", e);
    }
  }

  private ResultActions deleteCustomersFromProduct(
      final TestProduct product,
      final List<TestCustomer> customers
  ) {
    try {
      return client.deleteSubresources(
          PRODUCTS_PATH + product.getId() + CUSTOMERS_PATH,
          serialized(customers)
      );
    } catch (Exception e) {
      throw new RuntimeException("Exception during deletion of customers from product", e);
    }
  }


  enum CreateRelationType {
    PRODUCT_TO_CUSTOMER,
    CUSTOMER_TO_PRODUCT
  }

  enum BreakRelationType {
    PRODUCT_FROM_CUSTOMER,
    CUSTOMER_FROM_PRODUCT
  }

}
