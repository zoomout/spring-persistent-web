package com.bogdan.persistentweb.utils;

import com.bogdan.persistentweb.web.dto.GetAllDto;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static com.bogdan.persistentweb.utils.SerializationUtils.deserialized;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

public class AssertionUtils {

  public static void assertPaginationResult(final ResultActions result) throws Exception {
    result.andDo(r -> {
      final GetAllDto allFrom = getAllFrom(r);
      assertThat(allFrom.getTotalPages(), notNullValue());
      assertThat(allFrom.getItems().size(), greaterThan(0));
    });
  }

  private static GetAllDto getAllFrom(final MvcResult result) throws java.io.IOException {
    return deserialized(result.getResponse().getContentAsString(), GetAllDto.class);
  }
}
