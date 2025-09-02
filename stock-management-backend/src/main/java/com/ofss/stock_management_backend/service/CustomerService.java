package com.ofss.stock_management_backend.service;

import java.util.List;

import com.ofss.stock_management_backend.model.Customer;
import com.ofss.stock_management_backend.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmailId(email);
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteCustomer(String id) {
        customerRepository.deleteByEmailId(id);
    }

    // Register customer
    public Customer registerCustomer(Customer customer) {
        // check if email already exists
        if (customerRepository.findByEmailId(customer.getEmailId()) != null) {
            throw new RuntimeException("Email already registered!");
        }
        // hash the password before saving
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(hashedPassword);
        return customerRepository.save(customer);
    }

    // Authenticate user
    public Customer authenticate(String email, String password) {
        Customer customer = customerRepository.findByEmailId(email);
        if (customer != null) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(password, customer.getPassword())) {
                return customer;
            }
        }
        return null;
    }
}
