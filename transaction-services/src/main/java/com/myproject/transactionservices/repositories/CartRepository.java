package com.myproject.transactionservices.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myproject.transactionservices.models.Cart;
import com.myproject.transactionservices.models.Status;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{
    Cart findByUserIdAndStatus(Long userId, Status open);
}
