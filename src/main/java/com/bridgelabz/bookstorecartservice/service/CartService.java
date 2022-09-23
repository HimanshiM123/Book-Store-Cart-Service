package com.bridgelabz.bookstorecartservice.service;

import com.bridgelabz.bookstorecartservice.DTO.CartDTO;
import com.bridgelabz.bookstorecartservice.exception.CartException;
import com.bridgelabz.bookstorecartservice.model.CartModel;
import com.bridgelabz.bookstorecartservice.repository.ICartRepository;
import com.bridgelabz.bookstorecartservice.util.BookResponse;
import com.bridgelabz.bookstorecartservice.util.Response;
import com.bridgelabz.bookstorecartservice.util.TokenUtil;
import com.bridgelabz.bookstorecartservice.util.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CartService implements ICartService {

    @Autowired
    ICartRepository cartRepository;
    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    MailService mailService;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public Response addBooksToCart(CartDTO cartDTO, String token, Long bookId, Long orderQuantity) {
       UserResponse isUserPresent = restTemplate.getForObject("http://BS-USER-SERVICE:8083/user/verify/" + token, UserResponse.class);
        if (isUserPresent.getErrorCode() == 200) {
            Long userId = tokenUtil.decodeToken(token);
            Optional<CartModel> cartModel = cartRepository.findByIdAndUserId(userId, bookId);
            if (cartModel.isPresent()) {
                BookResponse isBookPresent = restTemplate.getForObject("http://BOOK-SERVICE:8084/books/verifyBook/" + bookId, BookResponse.class);
                if (isBookPresent.getErrorCode() ==200) {
                    CartModel cartModel1 = new CartModel(cartDTO);
                    cartModel1.setBookId(bookId);
                    cartModel1.setUserId(isUserPresent.getObject().getId());
                    if (isBookPresent.getObject().getQuantity() >= cartDTO.getQuantity()){
                        cartModel1.setQuantity(cartDTO.getQuantity());
                    } else {
                        throw new CartException(400, cartDTO.getQuantity() + "Not Available" + isBookPresent.getObject().getQuantity() + "Available");
                    }
                    cartModel1.setTotalPrice(cartDTO.getQuantity()*(isBookPresent.getObject().getPrice()));
                    cartRepository.save(cartModel1);
                    BookResponse updateBookQuantity = restTemplate.getForObject("http://BOOK-SERVICE:8084/books/changeBookQuantity/" + cartDTO.getQuantity() + bookId, BookResponse.class);
                    return new Response( "Book Added in Cart", 200,  cartModel1);
                }
            }
        }
        throw new CartException(400, "Book Not Added in Cart");
    }

    @Override
    public Response removeFromCart(String token, Long cartId) {
        UserResponse isUserPresent = restTemplate.getForObject("http://BS-USER-SERVICE:8083/user/verify/" + token, UserResponse.class);
        if (isUserPresent.getErrorCode() == 200) {
            Long userId = tokenUtil.decodeToken(token);
            Optional<CartModel> isCartPresent = cartRepository.findById(userId);
            if (isCartPresent.isPresent()) {
                if (isCartPresent.get().getUserId() == isUserPresent.getObject().getId()){
                    cartRepository.delete(isCartPresent.get());
                    BookResponse updateBookQuantity = restTemplate.getForObject("http://BOOK-SERVICE:8084/books/changeBookQuantity/" + isCartPresent.get(), BookResponse.class);
                    return new Response( "removed from cart", 200, isCartPresent.get());
                }
            }
        }
        throw new CartException(400, "Nothing in cart");
    }

    @Override
    public Response updateQuantity(String token, Long cartId, Long quantity) {
        UserResponse isUserPresent = restTemplate.getForObject("http://BS-USER-SERVICE:8083/user/verify/" + token, UserResponse.class);
        if (isUserPresent.getErrorCode() == 200) {
            Long userId = tokenUtil.decodeToken(token);
            Optional<CartModel> isCartPresent = cartRepository.findById(cartId);
            if (isCartPresent.isPresent()){
                BookResponse isBookPresent = restTemplate.getForObject("http://BOOK-SERVICE:8084/books/verifyBook/" + isCartPresent.get().getBookId(), BookResponse.class);
                if (isCartPresent.get().getUserId() == isUserPresent.getObject().getId()){
                    if (isCartPresent.get().getQuantity() > quantity){
                        Long bookQuantity = isCartPresent.get().getQuantity() - quantity;
                        isCartPresent.get().setQuantity(quantity);
                        isCartPresent.get().setTotalPrice((quantity) * (isBookPresent.getObject().getPrice()));
                        cartRepository.save(isCartPresent.get());
                        BookResponse updateBookQuantity = restTemplate.getForObject("http://BOOK-SERVICE:8084/books/changeBookQuantity/" + bookQuantity + isCartPresent.get().getBookId(), BookResponse.class);
                        return new Response("Updated", 200, isCartPresent.get());
                    } else {
                        Long bookQuantity = quantity - isCartPresent.get().getQuantity();
                        isCartPresent.get().setQuantity(quantity);
                        isCartPresent.get().setTotalPrice((quantity) * (isBookPresent.getObject().getPrice()));
                        cartRepository.save(isCartPresent.get());
                        BookResponse updateBookQuantity = restTemplate.getForObject("http://BOOK-SERVICE:8084/books/changeBookQuantity/" + bookQuantity + "/" + isCartPresent.get().getBookId(), BookResponse.class);
                        return new Response("Updated", 200, isCartPresent.get());
                    }
                }
                throw new CartException(400, "Not Found");
            }
        }
        throw new CartException(400, "Nothing in Cart");
    }

    @Override
    public List<CartModel> getAllCartItemForUser(String token) {
        boolean isUserPresent = restTemplate.getForObject("http://BS-USER-SERVICE:8083/user/verify/" + token, Boolean.class);
        if (isUserPresent) {
            Long userId = tokenUtil.decodeToken(token);
           Optional<CartModel> cart = cartRepository.findById(userId);
              if (cart.isPresent()){
                  List<CartModel> getAllCart = cartRepository.findAll();
                  if (getAllCart.size()>0){
                      return getAllCart;
                  } else
                      throw new CartException(400, "No Item In Cart");
              }
        }
        throw new CartException(400, "book not found in cart");
    }

    @Override
    public List<CartModel> getAllCartItem() {

        return cartRepository.findAll();
    }
}