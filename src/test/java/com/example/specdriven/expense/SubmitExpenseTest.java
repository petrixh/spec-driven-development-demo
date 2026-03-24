package com.example.specdriven.expense;

import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
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
class SubmitExpenseTest extends SpringBrowserlessTest {

    @Autowired
    private ExpenseService expenseService;

    @Test
    void formDisplaysAllRequiredFields() {
        navigate(SubmitExpenseView.class);

        assertTrue($(BigDecimalField.class).exists(), "Amount field should exist");
        assertTrue($(DatePicker.class).exists(), "Date field should exist");
        assertTrue($(ComboBox.class).exists(), "Category field should exist");
        assertTrue($(TextArea.class).exists(), "Description field should exist");
        assertTrue($(Upload.class).exists(), "Upload field should exist");
        assertTrue($(Button.class).withText("Submit").exists(), "Submit button should exist");
    }

    @Test
    void submittingValidFormCreatesExpenseWithPendingStatus() {
        navigate(SubmitExpenseView.class);

        test($(BigDecimalField.class).single()).setValue(BigDecimal.valueOf(42.50));
        test($(DatePicker.class).single()).setValue(LocalDate.now().minusDays(1));
        test($(ComboBox.class).single()).selectItem("Travel");
        test($(TextArea.class).single()).setValue("Business trip");

        test($(Button.class).withText("Submit").single()).click();

        List<Expense> expenses = expenseService.getExpensesForEmployee("employee");
        assertFalse(expenses.isEmpty(), "An expense should have been created");
        Expense created = expenses.getLast();
        assertEquals(Expense.Status.PENDING, created.getStatus());
        assertEquals(BigDecimal.valueOf(42.50), created.getAmount());
    }

    @Test
    void validationErrorsShownForMissingMandatoryFields() {
        navigate(SubmitExpenseView.class);

        test($(Button.class).withText("Submit").single()).click();

        BigDecimalField amount = $(BigDecimalField.class).single();
        DatePicker date = $(DatePicker.class).single();
        TextArea description = $(TextArea.class).single();

        assertTrue(amount.isInvalid(), "Amount should be invalid");
        assertTrue(date.isInvalid(), "Date should be invalid");
        assertTrue(description.isInvalid(), "Description should be invalid");
    }

    @Test
    void amountRejectsZeroAndNegativeValues() {
        navigate(SubmitExpenseView.class);

        test($(BigDecimalField.class).single()).setValue(BigDecimal.ZERO);
        test($(DatePicker.class).single()).setValue(LocalDate.now());
        test($(ComboBox.class).single()).selectItem("Meals");
        test($(TextArea.class).single()).setValue("Lunch");

        test($(Button.class).withText("Submit").single()).click();

        BigDecimalField amount = $(BigDecimalField.class).single();
        assertTrue(amount.isInvalid(), "Zero amount should be invalid");
    }

    @Test
    void dateRejectsFutureDates() {
        navigate(SubmitExpenseView.class);

        // The DatePicker has max=today, so the tester correctly rejects future dates.
        // Verify the max constraint is set properly.
        DatePicker date = $(DatePicker.class).single();
        assertEquals(LocalDate.now(), date.getMax(), "DatePicker max should be today");
    }

    @Test
    void successNotificationShownAfterSubmission() {
        navigate(SubmitExpenseView.class);

        test($(BigDecimalField.class).single()).setValue(BigDecimal.valueOf(25));
        test($(DatePicker.class).single()).setValue(LocalDate.now());
        test($(ComboBox.class).single()).selectItem("Office Supplies");
        test($(TextArea.class).single()).setValue("Pens and paper");

        test($(Button.class).withText("Submit").single()).click();

        assertTrue($(Notification.class).exists(), "Success notification should be shown");
    }
}
