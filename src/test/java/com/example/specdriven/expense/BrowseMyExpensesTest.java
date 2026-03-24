package com.example.specdriven.expense;

import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WithMockUser(username = "employee", roles = "EMPLOYEE")
class BrowseMyExpensesTest extends SpringBrowserlessTest {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseRepository expenseRepository;

    @BeforeEach
    void seedData() {
        expenseRepository.deleteAll();

        Expense e1 = new Expense();
        e1.setAmount(BigDecimal.valueOf(50));
        e1.setDate(LocalDate.now().minusDays(2));
        e1.setCategory(Expense.Category.TRAVEL);
        e1.setDescription("Taxi");
        e1.setEmployeeUsername("employee");
        expenseService.submit(e1);

        Expense e2 = new Expense();
        e2.setAmount(BigDecimal.valueOf(30));
        e2.setDate(LocalDate.now().minusDays(1));
        e2.setCategory(Expense.Category.MEALS);
        e2.setDescription("Lunch");
        e2.setEmployeeUsername("employee");
        expenseService.submit(e2);

        // Another user's expense — should not appear
        Expense e3 = new Expense();
        e3.setAmount(BigDecimal.valueOf(100));
        e3.setDate(LocalDate.now());
        e3.setCategory(Expense.Category.OTHER);
        e3.setDescription("Other user expense");
        e3.setEmployeeUsername("other");
        expenseService.submit(e3);
    }

    @Test
    void gridDisplaysExpenseColumns() {
        navigate(BrowseMyExpensesView.class);

        Grid grid = $(Grid.class).single();
        assertNotNull(grid);
        assertEquals(5, grid.getColumns().size());
    }

    @Test
    void onlyLoggedInEmployeeExpensesShown() {
        navigate(BrowseMyExpensesView.class);

        Grid<Expense> grid = $(Grid.class).single();
        int rowCount = test(grid).size();
        assertEquals(2, rowCount, "Only employee's expenses should be shown");
    }

    @Test
    void filteringByStatusUpdatesGridAndTotal() {
        navigate(BrowseMyExpensesView.class);

        Grid<Expense> grid = $(Grid.class).single();
        assertEquals(2, test(grid).size());

        // Filter by "Approved" — should show 0
        ComboBox<String> statusFilter = $(ComboBox.class).single();
        test(statusFilter).selectItem("Approved");

        assertEquals(0, test(grid).size());

        Span total = $(Span.class).withCondition(s -> s.getText().startsWith("Total:")).single();
        assertEquals("Total: $0.00", total.getText());
    }

    @Test
    void filteringByDateRangeUpdatesGrid() {
        navigate(BrowseMyExpensesView.class);

        List<DatePicker> datePickers = $(DatePicker.class).all();
        DatePicker fromDate = datePickers.get(0);
        DatePicker toDate = datePickers.get(1);

        test(fromDate).setValue(LocalDate.now().minusDays(1));
        test(toDate).setValue(LocalDate.now().minusDays(1));

        Grid<Expense> grid = $(Grid.class).single();
        assertEquals(1, test(grid).size());
    }

    @Test
    void defaultSortIsDateDescending() {
        navigate(BrowseMyExpensesView.class);

        Grid<Expense> grid = $(Grid.class).single();
        String firstRowDescription = test(grid).getCellText(0, 2);
        assertEquals("Lunch", firstRowDescription);
    }

    @Test
    void totalReflectsCurrentFilter() {
        navigate(BrowseMyExpensesView.class);

        Span total = $(Span.class).withCondition(s -> s.getText().startsWith("Total:")).single();
        assertEquals("Total: $80.00", total.getText());
    }
}
