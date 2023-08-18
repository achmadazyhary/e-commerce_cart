package com.myproject.productservices.services;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.myproject.productservices.dto.CreateProductRequest;
import com.myproject.productservices.dto.ProductResponse;
import com.myproject.productservices.dto.UpdateProductRequest;
import com.myproject.productservices.models.Product;
import com.myproject.productservices.repositories.ProductRepository;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::tProductResponse).toList();
    }

    @Transactional
    public ProductResponse create(CreateProductRequest request) {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setCreated_at(timestamp);
        product.setUpdated_at(timestamp);

        productRepository.save(product);

        return tProductResponse(product);
    }

    @Transactional
    public ProductResponse update(UpdateProductRequest request) {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Product product = productRepository.findById(request.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setCreated_at(timestamp);
        product.setUpdated_at(timestamp);

        productRepository.save(product);

        return tProductResponse(product);
    } 

    public void delete(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        productRepository.delete(product);
    }

    private ProductResponse tProductResponse(Product product) {
        return ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .created_at(product.getCreated_at())
            .updated_at(product.getUpdated_at())
            .build();
    }


}
