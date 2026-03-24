package com.example.specdriven.expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense {

    public enum Status {
        PENDING, APPROVED, REJECTED
    }

    public enum Category {
        TRAVEL("Travel"),
        MEALS("Meals"),
        OFFICE_SUPPLIES("Office Supplies"),
        OTHER("Other");

        private final String label;

        Category(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private Long id;
    private BigDecimal amount;
    private LocalDate date;
    private Category category;
    private String description;
    private byte[] receipt;
    private String receiptFileName;
    private String receiptMimeType;
    private Status status;
    private String employeeUsername;
    private String rejectionComment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getReceipt() {
        return receipt;
    }

    public void setReceipt(byte[] receipt) {
        this.receipt = receipt;
    }

    public String getReceiptFileName() {
        return receiptFileName;
    }

    public void setReceiptFileName(String receiptFileName) {
        this.receiptFileName = receiptFileName;
    }

    public String getReceiptMimeType() {
        return receiptMimeType;
    }

    public void setReceiptMimeType(String receiptMimeType) {
        this.receiptMimeType = receiptMimeType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getEmployeeUsername() {
        return employeeUsername;
    }

    public void setEmployeeUsername(String employeeUsername) {
        this.employeeUsername = employeeUsername;
    }

    public String getRejectionComment() {
        return rejectionComment;
    }

    public void setRejectionComment(String rejectionComment) {
        this.rejectionComment = rejectionComment;
    }

    public boolean hasReceipt() {
        return receipt != null && receipt.length > 0;
    }
}
