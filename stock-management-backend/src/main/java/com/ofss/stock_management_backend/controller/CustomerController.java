package com.ofss.stock_management_backend.controller;

import java.util.List;

import com.ofss.stock_management_backend.model.Customer;
import com.ofss.stock_management_backend.service.CustomerService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // <--------ROUTES FOR ADMIN------------->
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteCustomer(@PathVariable String email) {
        customerService.deleteCustomer(email);
        return ResponseEntity.ok("Customer deleted successfully: " + email);
    }
}
