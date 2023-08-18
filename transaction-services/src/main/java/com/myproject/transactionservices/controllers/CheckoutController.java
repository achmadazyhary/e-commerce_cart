package com.myproject.transactionservices.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.transactionservices.dto.CheckoutRequest;
import com.myproject.transactionservices.dto.WebResponse;
import com.myproject.transactionservices.services.CheckoutService;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {
    
    @Autowired
    private CheckoutService checkoutService;


    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> checkout(@RequestBody CheckoutRequest request) {
        checkoutService.processCheckout(request);
        return WebResponse.<String>builder()
            .messages("Checkout Successfully.").data("OK").build();
    }
}
