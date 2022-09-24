package com.bridgelabz.bookstorecartservice.service;

import com.bridgelabz.bookstorecartservice.DTO.CartDTO;
import com.bridgelabz.bookstorecartservice.model.CartModel;
import com.bridgelabz.bookstorecartservice.util.Response;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICartService {
    Response addBooksToCart(CartDTO cartDTO, String token, Long bookId);

    Response updateQuantity(String token, Long bookId, Long orderQuantity);

    Response removeFromCart(String token, Long cartId);

    List<CartModel> getAllCartItemForUser(String token);

    List<CartModel> getAllCartItem();
}
