package com.bridgelabz.bookstorecartservice.model;

import com.bridgelabz.bookstorecartservice.DTO.CartDTO;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "carts")
@Data
public class CartModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    private Long bookId;
    private Long quantity;
    private Long totalPrice;

    public CartModel(CartDTO cartDTO) {
        this.quantity = cartDTO.getQuantity();
        this.totalPrice = cartDTO.getTotalPrice();
    }

    public CartModel() {

    }
}
