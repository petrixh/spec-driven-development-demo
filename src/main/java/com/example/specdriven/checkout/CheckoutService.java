package com.example.specdriven.checkout;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CheckoutService {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    public CheckoutService(ProductRepository productRepository,
                           CustomerRepository customerRepository,
                           TransactionRepository transactionRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    public Optional<Product> findProductByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode);
    }

    public Optional<Customer> findCustomerByNumber(String customerNumber) {
        return customerRepository.findByCustomerNumber(customerNumber);
    }

    @Transactional
    public Transaction completeTransaction(Transaction transaction, Customer customer) {
        transaction.setCustomer(customer);
        transaction.setPaymentMethod(Transaction.PaymentMethod.CARD);
        transaction.setStatus(Transaction.Status.COMPLETED);
        transaction.setCompletedAt(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction completeCashTransaction(Transaction transaction) {
        transaction.setPaymentMethod(Transaction.PaymentMethod.CASH);
        transaction.setStatus(Transaction.Status.COMPLETED);
        transaction.setCompletedAt(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public List<Customer> findCustomersBySearch(String search) {
        if (search == null || search.isBlank()) {
            return customerRepository.findAll();
        }
        return customerRepository.findByNameContainingIgnoreCaseOrCustomerNumberContainingIgnoreCase(search, search);
    }
}
