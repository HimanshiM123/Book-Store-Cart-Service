package com.bridgelabz.bookstorecartservice.util;

import com.bridgelabz.bookstorecartservice.DTO.BookDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class BookResponse {
    private String message;
    private int errorCode;
    private BookDTO Object;

    public BookResponse() {
    }
}
