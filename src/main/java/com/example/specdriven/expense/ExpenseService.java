package com.example.specdriven.expense;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository repository;

    public ExpenseService(ExpenseRepository repository) {
        this.repository = repository;
    }

    public Expense submit(Expense expense) {
        if (expense.getAmount() == null || expense.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be a positive number");
        }
        if (expense.getDate() == null) {
            throw new IllegalArgumentException("Date is required");
        }
        if (expense.getDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the future");
        }
        if (expense.getCategory() == null) {
            throw new IllegalArgumentException("Category is required");
        }
        if (expense.getDescription() == null || expense.getDescription().isBlank()) {
            throw new IllegalArgumentException("Description is required");
        }
        expense.setStatus(Expense.Status.PENDING);
        return repository.save(expense);
    }

    public List<Expense> getExpensesForEmployee(String username) {
        return repository.findByEmployeeUsername(username);
    }

    public List<Expense> getPendingExpenses() {
        return repository.findByStatus(Expense.Status.PENDING);
    }

    public Expense approve(Long expenseId) {
        Expense expense = repository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));
        if (expense.getStatus() != Expense.Status.PENDING) {
            throw new IllegalStateException("Only pending expenses can be approved");
        }
        expense.setStatus(Expense.Status.APPROVED);
        return repository.save(expense);
    }

    public Expense reject(Long expenseId, String comment) {
        if (comment == null || comment.isBlank()) {
            throw new IllegalArgumentException("Rejection comment is required");
        }
        Expense expense = repository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));
        if (expense.getStatus() != Expense.Status.PENDING) {
            throw new IllegalStateException("Only pending expenses can be rejected");
        }
        expense.setStatus(Expense.Status.REJECTED);
        expense.setRejectionComment(comment);
        return repository.save(expense);
    }

    public BigDecimal getTotalPendingAmount() {
        return repository.findByStatus(Expense.Status.PENDING).stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long getPendingCount() {
        return repository.findByStatus(Expense.Status.PENDING).size();
    }

    public BigDecimal getApprovedAmountThisMonth() {
        YearMonth currentMonth = YearMonth.now();
        return repository.findByStatus(Expense.Status.APPROVED).stream()
                .filter(e -> YearMonth.from(e.getDate()).equals(currentMonth))
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<Expense.Category, BigDecimal> getApprovedByCategory() {
        return repository.findByStatus(Expense.Status.APPROVED).stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)
                ));
    }

    public List<Expense> findAll() {
        return repository.findAll();
    }
}
