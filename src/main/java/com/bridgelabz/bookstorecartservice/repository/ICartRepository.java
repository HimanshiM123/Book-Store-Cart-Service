package com.bridgelabz.bookstorecartservice.repository;

import com.bridgelabz.bookstorecartservice.model.CartModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICartRepository extends JpaRepository<CartModel, Long> {
    Optional<CartModel> findByIdAndUserId(Long userId, Long bookId);
}
