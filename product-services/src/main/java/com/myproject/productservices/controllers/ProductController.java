package com.myproject.productservices.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.productservices.dto.CreateProductRequest;
import com.myproject.productservices.dto.ProductResponse;
import com.myproject.productservices.dto.UpdateProductRequest;
import com.myproject.productservices.dto.WebResponse;
import com.myproject.productservices.services.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;

    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ProductResponse>> list() {
        List<ProductResponse> list = productService.list();
        return WebResponse.<List<ProductResponse>>builder().data(list).build();
    }

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProductResponse> create(@RequestBody CreateProductRequest request) {
        ProductResponse productResponse = productService.create(request);
        return WebResponse.<ProductResponse>builder().data(productResponse).build();
    }

    @PatchMapping(
        path = "/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProductResponse> update(@RequestBody UpdateProductRequest request, 
            @PathVariable("id") Long id) {
        request.setId(id);
        ProductResponse productResponse = productService.update(request);
        return WebResponse.<ProductResponse>builder().data(productResponse).build();
    }

    @DeleteMapping(
        path = "/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return WebResponse.<String>builder().data("OK").build();
    }

}
