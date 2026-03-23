package com.example.specdriven.expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpenseService {

    private final ExpenseRepository repository;

    public ExpenseService(ExpenseRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Expense submit(Expense expense) {
        expense.setStatus(ExpenseStatus.PENDING);
        return repository.save(expense);
    }

    public List<Expense> findByUser(String username, ExpenseStatus status,
                                     LocalDate fromDate, LocalDate toDate) {
        return repository.findFiltered(username, status, fromDate, toDate);
    }

    public List<Expense> findPending() {
        return repository.findByStatusOrderByDateDesc(ExpenseStatus.PENDING);
    }

    public Expense findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public Expense approve(Long id) {
        Expense expense = repository.findById(id).orElseThrow();
        expense.setStatus(ExpenseStatus.APPROVED);
        return repository.save(expense);
    }

    @Transactional
    public Expense reject(Long id, String comment) {
        Expense expense = repository.findById(id).orElseThrow();
        expense.setStatus(ExpenseStatus.REJECTED);
        expense.setReviewerComment(comment);
        return repository.save(expense);
    }

    public BigDecimal getTotalPendingAmount() {
        return repository.sumAmountByStatus(ExpenseStatus.PENDING);
    }

    public BigDecimal getApprovedAmountThisMonth() {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());
        return repository.sumAmountByStatusAndDateBetween(
                ExpenseStatus.APPROVED, startOfMonth, endOfMonth);
    }

    public long getPendingCount() {
        return repository.countByStatus(ExpenseStatus.PENDING);
    }

    public Map<ExpenseCategory, BigDecimal> getApprovedByCategory() {
        List<Object[]> results = repository.sumAmountByStatusGroupedByCategory(ExpenseStatus.APPROVED);
        Map<ExpenseCategory, BigDecimal> map = new LinkedHashMap<>();
        for (Object[] row : results) {
            map.put((ExpenseCategory) row[0], (BigDecimal) row[1]);
        }
        return map;
    }
}
