package com.bogdan.persistentweb.utils;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class ApiClient {

  private MockMvc mockMvc;

  public ApiClient(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  public ResultActions post(String path, String content) throws Exception {
    return this.mockMvc.perform(MockMvcRequestBuilders
        .post(path).header(CONTENT_TYPE, APPLICATION_JSON).content(content));
  }

  public ResultActions get(String path, String id) throws Exception {
    return this.mockMvc.perform(MockMvcRequestBuilders.get(path + id));
  }

  public ResultActions getAll(String path) throws Exception {
    return this.mockMvc.perform(MockMvcRequestBuilders.get(path));
  }

  public ResultActions delete(String path, String id) throws Exception {
    return this.mockMvc.perform(MockMvcRequestBuilders.delete(path + id));
  }

}
