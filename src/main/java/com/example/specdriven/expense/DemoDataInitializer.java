package com.example.specdriven.expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoDataInitializer implements CommandLineRunner {

    private final ExpenseService expenseService;

    public DemoDataInitializer(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @Override
    public void run(String... args) {
        // Pending expenses
        createExpense("employee", new BigDecimal("42.50"), LocalDate.now().minusDays(1),
                ExpenseCategory.MEALS, "Team lunch at downtown cafe");
        createExpense("employee", new BigDecimal("250.00"), LocalDate.now().minusDays(3),
                ExpenseCategory.TRAVEL, "Train tickets to client meeting");
        createExpense("employee", new BigDecimal("89.99"), LocalDate.now().minusDays(5),
                ExpenseCategory.OFFICE_SUPPLIES, "Ergonomic keyboard");
        createExpense("manager", new BigDecimal("150.00"), LocalDate.now().minusDays(2),
                ExpenseCategory.MEALS, "Client dinner");

        // Approved expenses (for dashboard chart)
        Expense approved1 = createExpense("employee", new BigDecimal("320.00"), LocalDate.now().minusDays(7),
                ExpenseCategory.TRAVEL, "Flight to conference");
        expenseService.approve(approved1.getId());
        Expense approved2 = createExpense("employee", new BigDecimal("75.00"), LocalDate.now().minusDays(10),
                ExpenseCategory.MEALS, "Client lunch");
        expenseService.approve(approved2.getId());
        Expense approved3 = createExpense("manager", new BigDecimal("199.99"), LocalDate.now().minusDays(4),
                ExpenseCategory.OFFICE_SUPPLIES, "Monitor stand");
        expenseService.approve(approved3.getId());
    }

    private Expense createExpense(String username, BigDecimal amount, LocalDate date,
                                   ExpenseCategory category, String description) {
        Expense expense = new Expense();
        expense.setAmount(amount);
        expense.setDate(date);
        expense.setCategory(category);
        expense.setDescription(description);
        expense.setSubmitterUsername(username);
        return expenseService.submit(expense);
    }
}
