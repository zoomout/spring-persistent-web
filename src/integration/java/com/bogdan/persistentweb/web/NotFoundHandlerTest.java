package com.bogdan.persistentweb.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class NotFoundHandlerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void testNotFound() throws Exception {
    final String notFoundPath = "/notFoundPath";
    this.mockMvc.perform(MockMvcRequestBuilders.get(notFoundPath))
        .andExpect(status().isNotFound())
        .andExpect(content().string("{\"message\":\"Resource not found: GET " + notFoundPath + "\"}"));
  }
}
