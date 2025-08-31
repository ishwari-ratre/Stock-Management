package com.ofss.service;

import java.util.List;
import com.ofss.model.Customer;
import com.ofss.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(int id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteCustomer(int id) {
        customerRepository.deleteById(id);
    }

     // Register customer
    public Customer registerCustomer(Customer customer) {
        // check if email already exists
        if (customerRepository.findByEmail(customer.getEmailId()) != null) {
            throw new RuntimeException("Email already registered!");
        }
        return customerRepository.save(customer);
    }

    // Authenticate user
    public Customer authenticate(String email, String password) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer != null && customer.getPassword().equals(password)) {
            return customer;
        }
        return null;
    }
}
