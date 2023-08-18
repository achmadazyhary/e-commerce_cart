package com.myproject.transactionservices.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myproject.transactionservices.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    
}
