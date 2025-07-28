package org.example.capstone1day26.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MerchantStock {

    @NotEmpty(message = "MerchantStock ID Cannot Be Empty")
    private String ID;

    @NotEmpty(message = "ProductID Cannot Be Empty")
    private String productID;

    @NotEmpty(message = "merchantID Cannot Be Empty")
    private String merchantID;

    @NotNull(message = "Stock cannot be empty")
    @Min(value = 10,message = "Stock Has to be more than 10")
    private int stock;

    @PositiveOrZero
    private int purchaseCount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate stockAddedDate;

    private boolean isClearance;

    private double clearancePrice;


}
