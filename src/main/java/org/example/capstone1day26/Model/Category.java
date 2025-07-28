package org.example.capstone1day26.Model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category {
    @NotEmpty(message = "Category ID Cannot Be Empty")
    private String ID;
    @NotEmpty(message = "Name Cannot Be Empty")
    @Size(min = 3,message = "Name length is at least 3 or above")
    private String name;
}
