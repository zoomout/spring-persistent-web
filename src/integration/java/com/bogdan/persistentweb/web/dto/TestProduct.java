package com.bogdan.persistentweb.web.dto;

import java.util.Objects;

public class TestProduct {

  private String id;
  private String title;

  public String getId() {
    return id;
  }

  public TestProduct setId(String id) {
    this.id = id;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public TestProduct setTitle(String title) {
    this.title = title;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TestProduct that = (TestProduct) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(title, that.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title);
  }

  @Override
  public String toString() {
    return "Product{" +
        "id='" + id + '\'' +
        ", title='" + title + '\'' +
        '}';
  }
}
