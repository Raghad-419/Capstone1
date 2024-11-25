package com.example.capstone1.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Merchant {
    @NotEmpty(message = "Empty ID")
    private String id;
    @NotEmpty(message = "Empty name")
    @Size(min = 4,message = "Name have to be more than 3 length long")
    private String name;
}
