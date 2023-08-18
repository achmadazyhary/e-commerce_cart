package com.myproject.chartservices.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.myproject.chartservices.dto.CartItemResponse;
import com.myproject.chartservices.dto.CreateCartRequest;
import com.myproject.chartservices.models.Cart;
import com.myproject.chartservices.models.CartItem;
import com.myproject.chartservices.models.Product;
import com.myproject.chartservices.models.Status;
import com.myproject.chartservices.repositories.CartItemRepository;
import com.myproject.chartservices.repositories.CartRepository;
import com.myproject.chartservices.repositories.ProductRepository;

@Service
public class CartServices {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional(readOnly = true)
    public List<CartItemResponse> getCartItems(Long userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Status.open);
        if (cart == null) {
            return Collections.emptyList();
        }

        List<CartItemResponse> cartItemResponses = new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems()) {
            CartItemResponse response = new CartItemResponse();
            response.setProductId(cartItem.getProduct().getId());
            response.setProductName(cartItem.getProduct().getName());
            response.setProductPrice(cartItem.getProduct().getPrice());
            response.setQuantity(cartItem.getQuantity());
            response.setCreated_at(cartItem.getCreated_at());
            response.setUpdated_at(cartItem.getUpdated_at());
            cartItemResponses.add(response);
        }

        return cartItemResponses;
    }

    @Transactional
    public CartItemResponse add(CreateCartRequest request) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Cart cart = cartRepository.findByUserIdAndStatus(request.getUserId(), Status.open);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(request.getUserId());
            cart.setStatus(Status.open);
            cart.setCreated_at(timestamp);
            cart.setUpdated_at(timestamp);
        }

        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(request.getQuantity());
        cartItem.setCreated_at(timestamp);
        cartItem.setUpdated_at(timestamp);
        
        cartRepository.save(cart);
        cartItemRepository.save(cartItem);

        return toCartItemResponse(cartItem);
    }

    @Transactional
    public CartItemResponse updateCartItem(Long userId, Long productId, int quantity) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Status.open);
        if (cart == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No open cart found for the user.");
        }

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        CartItem cartItem = findCartItemByProduct(cart, product);

        if (cartItem == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found in the cart.");
        }

        // Update jumlah produk dalam keranjang belanja
        cartItem.setQuantity(quantity);

        // Simpan perubahan ke dalam basis data
        cartItemRepository.save(cartItem);

        return toCartItemResponse(cartItem);
    }

    @Transactional
    public void deleteCartItem(Long userId, Long productId) {
        // Ambil keranjang belanja yang terbuka (status open) untuk pengguna tertentu
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Status.open);
        if (cart == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No open cart found for the user.");
        }

        // Cari produk berdasarkan productId
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found"));

        // Cari item produk dalam keranjang belanja
        CartItem cartItem = findCartItemByProduct(cart, product);

        if (cartItem == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found in the cart.");
        }

        // Simpan perubahan ke dalam basis data
        cartItemRepository.delete(cartItem);
    }

    private CartItem findCartItemByProduct(Cart cart, Product product) {
        for (CartItem cartItem : cart.getCartItems()) {
            if (cartItem.getProduct().equals(product)) {
                return cartItem;
            }
        }
        return null;
    }

    private CartItemResponse toCartItemResponse(CartItem cartItem) {
        return CartItemResponse.builder()
            .productId(cartItem.getProduct().getId())
            .productName(cartItem.getProduct().getName())
            .productPrice(cartItem.getProduct().getPrice())
            .quantity(cartItem.getQuantity())
            .created_at(cartItem.getCreated_at())
            .updated_at(cartItem.getUpdated_at())
            .build();
    }
    
}
