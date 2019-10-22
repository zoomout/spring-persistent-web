package com.bogdan.persistentweb.web;

import com.bogdan.persistentweb.web.dto.TestProduct;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.stream.Stream;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String PRODUCTS = "/products";
    private static final String PRODUCTS_DIR = PRODUCTS + "/";

    private static Stream<Arguments> validContent() {
        return Stream.of(
                Arguments.of(generateProductDto()),
                Arguments.of(generateProductDto().setId("shouldBeIgnored"))
        );
    }

    @ParameterizedTest
    @MethodSource("validContent")
    public void callingPost_shouldCreateProduct(final TestProduct product) throws Exception {
        MvcResult result = this.mockMvc
                .perform(
                        post(PRODUCTS)
                                .header(CONTENT_TYPE, APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(product))
                )
                .andExpect(status().isCreated()).andReturn();
        assertThat(getProductId(result), greaterThan(0));
    }

    private static Stream<Arguments> invalidContent() {
        return Stream.of(
                Arguments.of("empty object" , "{}"),
                Arguments.of("empty string", ""),
                Arguments.of("absent title", "{\"id\":1}")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidContent")
    public void callingPost_withInvalidContent_shouldReturn400(final String content) throws Exception {
        this.mockMvc
                .perform(
                        post(PRODUCTS)
                                .header(CONTENT_TYPE, APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isBadRequest());
    }

    private static TestProduct generateProductDto() {
        return new TestProduct().setTitle("product_" + randomAlphanumeric(6));
    }

    private int getProductId(MvcResult result) {
        final String locationHeader = result.getResponse().getHeader(LOCATION);
        assertThat(locationHeader, notNullValue());
        assertThat(locationHeader.length(), greaterThan(PRODUCTS_DIR.length()));
        return Integer.parseInt(locationHeader.substring(PRODUCTS_DIR.length()));
    }

}
