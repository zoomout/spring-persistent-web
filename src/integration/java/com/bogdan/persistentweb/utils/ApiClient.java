package com.bogdan.persistentweb.utils;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class ApiClient {

    private String url;
    private MockMvc mockMvc;

    public ApiClient(String url, MockMvc mockMvc) {
        this.url = url;
        this.mockMvc = mockMvc;
    }

    public ResultActions post(String content) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders
                .post(url).header(CONTENT_TYPE, APPLICATION_JSON).content(content));
    }

    public ResultActions get(String id) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders.get(url + id));
    }

    public ResultActions delete(String id) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders.delete(url + id));
    }

}
