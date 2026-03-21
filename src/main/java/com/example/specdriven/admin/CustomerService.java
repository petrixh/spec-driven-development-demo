package com.example.specdriven.admin;

import com.example.specdriven.checkout.Customer;
import com.example.specdriven.checkout.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Transactional
    public Customer save(Customer customer) {
        customerRepository.findByCustomerNumber(customer.getCustomerNumber()).ifPresent(existing -> {
            if (!existing.getId().equals(customer.getId())) {
                throw new IllegalArgumentException(
                        "A customer with card number '" + customer.getCustomerNumber() + "' already exists.");
            }
        });
        return customerRepository.save(customer);
    }

    @Transactional
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }
}
