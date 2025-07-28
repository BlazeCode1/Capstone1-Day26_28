package org.example.capstone1day26.Model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Product {
    @NotEmpty(message = "Product ID cannot be Empty")
    private String ID;

    @NotEmpty(message = "Product Name Cannot Be Empty")
    @Size(min = 3,message = "Product Name have to be 3 length or more")
    private String name;

    @NotNull
    @Positive(message = "Must Be Positive Number")
    private double price;

    @NotEmpty(message = "category ID must not be empty")
    private String categoryID;

    @PositiveOrZero
    private int totalSold;

    @PositiveOrZero
    private double totalRevenue;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastSoldDate;

    @Pattern(
            regexp = "^[a-zA-Z0-9]+:[a-zA-Z]+:[a-zA-Z0-9]+$",
            message = "Product code must follow the format: productName:color:code"
    )
    @NotEmpty(message = "Product code Must Not be Empty")
    private String productCode;

}
