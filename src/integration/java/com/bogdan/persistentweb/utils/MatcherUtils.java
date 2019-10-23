package com.bogdan.persistentweb.utils;

import org.hamcrest.Matcher;

import static org.hamcrest.text.MatchesPattern.matchesPattern;

public class MatcherUtils {

  public static Matcher<String> jsonArrayMatcher() {
    return matchesPattern("^\\[.*\\]$");
  }
}
