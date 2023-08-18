package com.myproject.chartservices.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.myproject.chartservices.models.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    
}
