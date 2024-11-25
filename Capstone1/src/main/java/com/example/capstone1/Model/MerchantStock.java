package com.example.capstone1.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MerchantStock {
    @NotEmpty(message = "Empty ID")
    private String id;
    @NotEmpty(message = "Empty product ID")
    private String productId ;
    @NotEmpty(message = "Empty merchant ID")
    private String merchantId;
    @NotNull(message = "Empty stock")
    @Positive(message = "Stock should be more than 0")
    @Min(value = 11,message = "stock have to be more than 10 at start")
    private int stock;

}
