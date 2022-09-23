package com.bridgelabz.bookstorecartservice.controller;

import com.bridgelabz.bookstorecartservice.DTO.CartDTO;
import com.bridgelabz.bookstorecartservice.model.CartModel;
import com.bridgelabz.bookstorecartservice.service.ICartService;
import com.bridgelabz.bookstorecartservice.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    ICartService cartService;

    @PostMapping(value = "/addBooksToCart")
    ResponseEntity<Response> addToCart(@RequestBody CartDTO cartDTO, @RequestHeader String token, @PathVariable Long bookId, @PathVariable Long orderQuantity) {
        Response response = cartService.addBooksToCart(cartDTO, token, bookId, orderQuantity);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/updateQuantity")
    ResponseEntity<Response> updateQuantity(@RequestHeader String token, @PathVariable Long bookId, @PathVariable Long orderQuantity) {
        Response response = cartService.updateQuantity(token, bookId, orderQuantity);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/removeFromCart")
    ResponseEntity<Response> removeFromCart(@RequestHeader String token, @PathVariable Long cartId) {
        Response response = cartService.removeFromCart(token, cartId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllCartItemForUser")
    public List<CartModel> getAllCartItemForUser(@RequestHeader String token){
        return cartService.getAllCartItemForUser(token);
    }

    @GetMapping("/getAllCartItem")
    public List<CartModel> getAllCartItem(){
        return cartService.getAllCartItem();
    }
}
