package com.bogdan.persistentweb.web.dto;

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
    public String toString() {
        return "TestProduct{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
