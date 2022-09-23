package com.bridgelabz.bookstorecartservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class CartDTO {
    private Long quantity;
    private Long totalPrice;
}
