package com.bridgelabz.bookstorecartservice.exception;

public class CartException extends RuntimeException{
    private int statusCode;
    private String statusMessage;

    public CartException(int statusCode, String statusMessage){
        super(statusMessage);
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}
