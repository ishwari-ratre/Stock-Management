package com.ofss.stock_management_backend.controller;

import java.util.List;
import java.util.Optional;

import com.ofss.stock_management_backend.dto.DashboardResponse;
import com.ofss.stock_management_backend.model.Customer;
import com.ofss.stock_management_backend.repository.CustomerRepository;
import com.ofss.stock_management_backend.service.CustomerService;
import com.ofss.stock_management_backend.util.JwtUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final JwtUtil jwtUtil;

    public CustomerController(CustomerService customerService, JwtUtil jwtUtil, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
        this.customerRepository=customerRepository;
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

    @PatchMapping("/update")
    public ResponseEntity<?> patchCustomer(@RequestBody Customer updatedCustomer) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // Extracted from JWT subject

        Customer existingCustomer = customerRepository.findByEmailId(email);
        if (existingCustomer == null) {
            return ResponseEntity.badRequest().body("Customer not found for email: " + email);
        }

        // Update only non-null or valid fields
        if (updatedCustomer.getFirstName() != "") existingCustomer.setFirstName(updatedCustomer.getFirstName());
        if (updatedCustomer.getLastName() != "") existingCustomer.setLastName(updatedCustomer.getLastName());
        if (updatedCustomer.getCity() != "") existingCustomer.setCity(updatedCustomer.getCity());
        if (updatedCustomer.getPhoneNumber() != 0) existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());

        customerRepository.save(existingCustomer);
        return ResponseEntity.ok(existingCustomer);
    }
}
