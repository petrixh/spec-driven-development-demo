package com.example.specdriven.expense;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ExpenseRepository {

    private final Map<Long, Expense> expenses = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Expense save(Expense expense) {
        if (expense.getId() == null) {
            expense.setId(idGenerator.getAndIncrement());
        }
        expenses.put(expense.getId(), expense);
        return expense;
    }

    public Optional<Expense> findById(Long id) {
        return Optional.ofNullable(expenses.get(id));
    }

    public List<Expense> findByEmployeeUsername(String username) {
        return expenses.values().stream()
                .filter(e -> e.getEmployeeUsername().equals(username))
                .toList();
    }

    public List<Expense> findByStatus(Expense.Status status) {
        return expenses.values().stream()
                .filter(e -> e.getStatus() == status)
                .toList();
    }

    public List<Expense> findAll() {
        return new ArrayList<>(expenses.values());
    }

    public void deleteAll() {
        expenses.clear();
    }
}
