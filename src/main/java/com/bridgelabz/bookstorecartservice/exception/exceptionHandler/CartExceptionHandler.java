package com.bridgelabz.bookstorecartservice.exception.exceptionHandler;

import com.bridgelabz.bookstorecartservice.exception.CartException;
import com.bridgelabz.bookstorecartservice.util.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

public class CartExceptionHandler {
    @ExceptionHandler(CartException.class)
    public ResponseEntity<Response> handleHiringException(CartException he){
        Response response=new Response();
        response.setErrorCode(400);
        response.setMessage(he.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Response> defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception{
        Response response = new Response();
        response.setErrorCode(400);
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
