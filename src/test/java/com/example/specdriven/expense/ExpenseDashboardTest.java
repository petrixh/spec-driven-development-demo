package com.example.specdriven.expense;

import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
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
class ExpenseDashboardTest extends SpringBrowserlessTest {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseRepository expenseRepository;

    @BeforeEach
    void seedData() {
        expenseRepository.deleteAll();

        // Pending expense
        Expense e1 = new Expense();
        e1.setAmount(BigDecimal.valueOf(100));
        e1.setDate(LocalDate.now().minusDays(1));
        e1.setCategory(Expense.Category.TRAVEL);
        e1.setDescription("Flight");
        e1.setEmployeeUsername("employee");
        expenseService.submit(e1);

        // Another pending expense
        Expense e2 = new Expense();
        e2.setAmount(BigDecimal.valueOf(50));
        e2.setDate(LocalDate.now());
        e2.setCategory(Expense.Category.MEALS);
        e2.setDescription("Dinner");
        e2.setEmployeeUsername("employee");
        expenseService.submit(e2);

        // Approved expense this month
        Expense e3 = new Expense();
        e3.setAmount(BigDecimal.valueOf(200));
        e3.setDate(LocalDate.now());
        e3.setCategory(Expense.Category.OFFICE_SUPPLIES);
        e3.setDescription("Monitor");
        e3.setEmployeeUsername("employee");
        expenseService.submit(e3);
        expenseService.approve(e3.getId());
    }

    @Test
    void dashboardShowsTotalPendingAmount() {
        navigate(ExpenseDashboardView.class);

        Div card = $(Div.class).withClassName("summary-card").all().stream()
                .filter(d -> {
                    var titles = $(Paragraph.class, d).withClassName("card-title").all();
                    return !titles.isEmpty() && titles.getFirst().getText().equals("Total Pending");
                })
                .findFirst().orElseThrow();

        Paragraph value = $(Paragraph.class, card).withClassName("card-value").single();
        assertEquals("$150.00", value.getText());
    }

    @Test
    void dashboardShowsApprovedAmountThisMonth() {
        navigate(ExpenseDashboardView.class);

        Div card = $(Div.class).withClassName("summary-card").all().stream()
                .filter(d -> {
                    var titles = $(Paragraph.class, d).withClassName("card-title").all();
                    return !titles.isEmpty() && titles.getFirst().getText().equals("Approved This Month");
                })
                .findFirst().orElseThrow();

        Paragraph value = $(Paragraph.class, card).withClassName("card-value").single();
        assertEquals("$200.00", value.getText());
    }

    @Test
    void dashboardShowsPendingCount() {
        navigate(ExpenseDashboardView.class);

        Div card = $(Div.class).withClassName("summary-card").all().stream()
                .filter(d -> {
                    var titles = $(Paragraph.class, d).withClassName("card-title").all();
                    return !titles.isEmpty() && titles.getFirst().getText().equals("Pending Count");
                })
                .findFirst().orElseThrow();

        Paragraph value = $(Paragraph.class, card).withClassName("card-value").single();
        assertEquals("2", value.getText());
    }

    @Test
    void chartDisplaysApprovedExpensesByCategory() {
        navigate(ExpenseDashboardView.class);

        assertTrue($(Chart.class).exists(), "Chart should exist when approved expenses are present");
    }

    @Test
    @WithMockUser(username = "employee", roles = "EMPLOYEE")
    void nonManagerCannotAccessDashboard() {
        try {
            navigate(ExpenseDashboardView.class);
            fail("Employee should not access dashboard");
        } catch (Exception e) {
            // Expected: access denied
        }
    }
}
