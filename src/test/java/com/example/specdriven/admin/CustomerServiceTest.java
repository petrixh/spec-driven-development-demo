package com.example.specdriven.admin;

import com.example.specdriven.checkout.Customer;
import com.example.specdriven.checkout.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void canSaveNewCustomer() {
        Customer customer = new Customer("Test Customer", "SVCTEST001");
        Customer saved = customerService.save(customer);
        assertNotNull(saved.getId());
        assertEquals("Test Customer", saved.getName());
    }

    @Test
    void duplicateCardNumberThrowsException() {
        // CARD001 already exists from sample data
        Customer duplicate = new Customer("Fake Customer", "CARD001");
        assertThrows(IllegalArgumentException.class, () -> customerService.save(duplicate));
    }

    @Test
    void canDeleteCustomer() {
        Customer customer = new Customer("Delete Me", "SVCDEL001");
        Customer saved = customerService.save(customer);
        assertNotNull(saved.getId());

        customerService.delete(saved);
        assertTrue(customerRepository.findByCustomerNumber("SVCDEL001").isEmpty());
    }

    @Test
    void findAllReturnsCustomers() {
        assertFalse(customerService.findAll().isEmpty());
    }
}
