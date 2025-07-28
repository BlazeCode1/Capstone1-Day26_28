package org.example.capstone1day26.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Merchant {
    @NotEmpty(message = "Merchant ID Cannot Be Empty")
    private String ID;
    @NotEmpty(message = "Merchant Name Cannot Be Empty")
    @Size(min = 3,message = "Merchant Name Can Only ")
    private String name;


    private double commissionRate;

    private boolean isSuspended;
}
