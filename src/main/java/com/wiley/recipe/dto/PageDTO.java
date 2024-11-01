package com.wiley.recipe.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class PageDTO<T> {
    private List<T> results;
    private int totalElements;
    private int totalPages;
    private int number;
    private int size;


    public PageDTO(List<T> elements, int totalElements, int number, int size) {
        this.totalElements = totalElements;
        this.number = number;
        this.size = size;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
        this.results = elements;
    }


}
