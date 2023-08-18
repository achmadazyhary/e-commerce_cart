package com.myproject.chartservices.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myproject.chartservices.models.Cart;
import com.myproject.chartservices.models.Status;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{

    Cart findByUserIdAndStatus(Long userId, Status open);
    
}
