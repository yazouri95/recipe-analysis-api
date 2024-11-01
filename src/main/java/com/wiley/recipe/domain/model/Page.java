package com.wiley.recipe.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {
    private List<T> results;
    private int totalElements;
    private int totalPages;
    private int number;
    private int size;


    public Page(List<T> elements, int totalElements, int number, int pageSize) {
        this.totalElements = totalElements;
        this.number = number;
        this.size = pageSize;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
        this.results = elements;
    }
}
