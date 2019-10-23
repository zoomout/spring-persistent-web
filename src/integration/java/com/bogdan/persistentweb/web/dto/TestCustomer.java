package com.bogdan.persistentweb.web.dto;

import java.util.Objects;

public class TestCustomer {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public TestCustomer setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TestCustomer setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestCustomer that = (TestCustomer) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", title='" + name + '\'' +
                '}';
    }
}
