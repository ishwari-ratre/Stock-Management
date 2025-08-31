package com.ofss.controller;

import com.ofss.model.Customer;
import com.ofss.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final CustomerService customerService;

    public AuthController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Customer customer) {
        try {
            Customer savedCustomer = customerService.registerCustomer(customer);
            return ResponseEntity.ok("Customer registered successfully with id: " + savedCustomer.getCustId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        Customer customer = customerService.authenticate(email, password);
        if (customer != null) {
            return ResponseEntity.ok("Login successful for customer: " + customer.getFirstName()+" "+customer.getLastName());
        } else {
            return ResponseEntity.status(401).body("Invalid credentials!");
        }
    }
}

