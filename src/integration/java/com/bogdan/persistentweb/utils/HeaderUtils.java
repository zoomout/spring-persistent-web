package com.bogdan.persistentweb.utils;

import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpHeaders.LOCATION;

public class HeaderUtils {

    public static String idFromLocationHeader(String url, MvcResult result) {
        final String locationHeader = result.getResponse().getHeader(LOCATION);
        assertThat(locationHeader, notNullValue());
        assertThat(locationHeader.length(), greaterThan(url.length()));
        return locationHeader.substring(url.length());
    }
}
