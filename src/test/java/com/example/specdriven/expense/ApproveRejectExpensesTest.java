package com.example.specdriven.expense;

import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextArea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WithMockUser(username = "manager", roles = {"MANAGER", "EMPLOYEE"})
class ApproveRejectExpensesTest extends SpringBrowserlessTest {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseRepository expenseRepository;

    private Expense pendingExpense;

    @BeforeEach
    void seedData() {
        expenseRepository.deleteAll();

        Expense e1 = new Expense();
        e1.setAmount(BigDecimal.valueOf(75));
        e1.setDate(LocalDate.now().minusDays(1));
        e1.setCategory(Expense.Category.TRAVEL);
        e1.setDescription("Flight ticket");
        e1.setEmployeeUsername("employee");
        pendingExpense = expenseService.submit(e1);

        // An already approved expense — should not show
        Expense e2 = new Expense();
        e2.setAmount(BigDecimal.valueOf(20));
        e2.setDate(LocalDate.now().minusDays(3));
        e2.setCategory(Expense.Category.MEALS);
        e2.setDescription("Coffee");
        e2.setEmployeeUsername("employee");
        expenseService.submit(e2);
        expenseService.approve(e2.getId());
    }

    @Test
    void gridShowsOnlyPendingExpenses() {
        navigate(ReviewExpensesView.class);

        Grid<Expense> grid = $(Grid.class).single();
        assertEquals(1, test(grid).size(), "Only pending expenses should be shown");
    }

    @Test
    void selectingExpenseShowsDetails() {
        navigate(ReviewExpensesView.class);

        Grid<Expense> grid = $(Grid.class).single();
        grid.select(pendingExpense);

        // Approve and Reject buttons should be visible
        assertTrue($(Button.class).withText("Approve").exists(), "Approve button should exist");
        assertTrue($(Button.class).withText("Reject").exists(), "Reject button should exist");
    }

    @Test
    void approveUpdatesStatusToApproved() {
        navigate(ReviewExpensesView.class);

        Grid<Expense> grid = $(Grid.class).single();
        grid.select(pendingExpense);

        test($(Button.class).withText("Approve").single()).click();

        assertEquals(Expense.Status.APPROVED, pendingExpense.getStatus());
        assertTrue($(Notification.class).exists(), "Approval notification should be shown");
    }

    @Test
    void rejectRequiresComment() {
        navigate(ReviewExpensesView.class);

        Grid<Expense> grid = $(Grid.class).single();
        grid.select(pendingExpense);

        test($(Button.class).withText("Reject").single()).click();

        // Dialog should open
        assertTrue($(Dialog.class).exists(), "Reject dialog should open");

        // Try confirming without comment
        test($(Button.class).withText("Confirm Rejection").single()).click();

        // Dialog should still be open (validation failed)
        TextArea commentField = $(TextArea.class).single();
        assertTrue(commentField.isInvalid(), "Comment field should be invalid without text");
    }

    @Test
    void rejectWithCommentUpdatesStatus() {
        navigate(ReviewExpensesView.class);

        Grid<Expense> grid = $(Grid.class).single();
        grid.select(pendingExpense);

        test($(Button.class).withText("Reject").single()).click();

        test($(TextArea.class).single()).setValue("Duplicate submission");
        test($(Button.class).withText("Confirm Rejection").single()).click();

        assertEquals(Expense.Status.REJECTED, pendingExpense.getStatus());
        assertEquals("Duplicate submission", pendingExpense.getRejectionComment());
    }

    @Test
    @WithMockUser(username = "employee", roles = "EMPLOYEE")
    void nonManagerCannotAccessView() {
        // Employee should not be able to navigate to ReviewExpensesView
        try {
            navigate(ReviewExpensesView.class);
            fail("Employee should not access review-expenses");
        } catch (Exception e) {
            // Expected: access denied
        }
    }
}
