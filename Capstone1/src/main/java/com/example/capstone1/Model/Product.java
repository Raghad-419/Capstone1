package com.example.capstone1.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class Product {
    @NotEmpty(message = "Empty ID")
    private String id;
    @NotEmpty(message = "Empty name")
    @Size(min = 4 ,message = "Name have to be more than 3 length long")
    private String name;
    @NotNull(message = "Empty price")
    @Positive(message = "Price should ")
    @Positive(message = "Price must be positive number and more than 0")
    private double price;
    @NotEmpty(message = "Empty category ID ")
    private String categoryId ;
    @NotEmpty(message = "Empty status")
    @Pattern(regexp = "Refundable|Non-refundable")
    private String status ;
    @Max(value = 5,message = "Rating should be less than 5")
    @PositiveOrZero(message = "Rating should be zero or positive")
    private double rating=0;
    private ArrayList<String> reviews = new ArrayList<>();
    @PositiveOrZero
    private int countOfSell=0;
}
