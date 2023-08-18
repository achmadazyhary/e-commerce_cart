package com.myproject.transactionservices.services;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.myproject.transactionservices.dto.CheckoutRequest;
import com.myproject.transactionservices.models.Cart;
import com.myproject.transactionservices.models.Payment;
import com.myproject.transactionservices.models.Product;
import com.myproject.transactionservices.models.User;
import com.myproject.transactionservices.models.Status;
import com.myproject.transactionservices.models.Transaction;
import com.myproject.transactionservices.repositories.CartRepository;
import com.myproject.transactionservices.repositories.PaymentRepository;
import com.myproject.transactionservices.repositories.ProductRepository;
import com.myproject.transactionservices.repositories.TransactionRepository;
import com.myproject.transactionservices.repositories.UserRepository;

@Service
public class CheckoutService {
    
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void processCheckout(CheckoutRequest checkoutRequest) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        Long userId = checkoutRequest.getUserId();
        List<Long> productIds = checkoutRequest.getProductIds();

        // Lakukan logika untuk memproses checkout
        // Misalnya, Anda dapat membuat transaksi baru dan pembayaran

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));

        Cart cart = cartRepository.findByUserIdAndStatus(userId, Status.open);
        if (cart == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No open cart found for the user.");
        }

        List<Product> products = productRepository.findAllById(productIds);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Product product : products) {
            totalAmount = totalAmount.add(product.getPrice());
        }

        // Buat transaksi baru
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setCart(cart);
        transaction.setTotalAmount(totalAmount);
        transaction.setTransactionDate(timestamp);
        transaction.setCreated_at(timestamp);
        transaction.setUpdated_at(timestamp);

        // Simpan transaksi ke dalam basis data
        transactionRepository.save(transaction);

        // Buat pembayaran
        Payment payment = new Payment();
        payment.setTransaction(transaction);
        payment.setPaymentMethod(checkoutRequest.getPaymentMethod()); // Anda dapat mengatur metode pembayaran sesuai kebutuhan
        payment.setPaymentStatus(checkoutRequest.getPaymentStatus());
        payment.setPaymentDate(timestamp); // Tanggal pembayaran kosong saat awal
        payment.setCreated_at(timestamp);
        payment.setUpdated_at(timestamp);

        // Simpan pembayaran ke dalam basis data
        paymentRepository.save(payment);

        // Ubah status keranjang belanja menjadi closed
        cart.setStatus(Status.closed);
        cartRepository.save(cart);
    }
}
