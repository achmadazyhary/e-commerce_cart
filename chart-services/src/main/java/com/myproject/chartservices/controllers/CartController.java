package com.myproject.chartservices.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.chartservices.dto.CartItemResponse;
import com.myproject.chartservices.dto.CreateCartRequest;
import com.myproject.chartservices.dto.UpdateCartItemRequest;
import com.myproject.chartservices.dto.WebResponse;
import com.myproject.chartservices.services.CartServices;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    CartServices cartServices;

    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<CartItemResponse>> getCartItems(@RequestParam Long userId) {
        List<CartItemResponse> cartItems = cartServices.getCartItems(userId);
        return WebResponse.<List<CartItemResponse>>builder()
            .messages("Successfully")
            .data(cartItems)
            .build();
    } 

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CartItemResponse> addToCart(@RequestBody CreateCartRequest request) {
        CartItemResponse data = cartServices.add(request);

        return WebResponse.<CartItemResponse>builder()
            .data(data)
            .messages("Product added to cart successfully")
            .build();
    }

    @PutMapping(
        path = "/{productId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CartItemResponse> updateCartItem(@PathVariable("productId") Long productId,@RequestBody UpdateCartItemRequest request, @RequestParam Long userId) {
        CartItemResponse data = cartServices.updateCartItem(userId, productId, request.getQuantity());
        return WebResponse.<CartItemResponse>builder()
            .data(data)
            .messages("Cart item updated successfully")
            .build();
    }

    @DeleteMapping(
        path = "/{productId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> deleteCartItem(@PathVariable("productId") Long productId, @RequestParam Long userId) {
        cartServices.deleteCartItem(userId, productId);
        return WebResponse.<String>builder()
            .messages("Cart item deleted successfully")
            .data("OK")
            .build();
    }
    
}
