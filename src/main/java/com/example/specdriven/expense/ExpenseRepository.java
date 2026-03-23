package com.example.specdriven.expense;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findBySubmitterUsernameOrderByDateDesc(String username);

    List<Expense> findBySubmitterUsernameAndStatusOrderByDateDesc(String username, ExpenseStatus status);

    @Query("SELECT e FROM Expense e WHERE e.submitterUsername = :username " +
           "AND (:status IS NULL OR e.status = :status) " +
           "AND (:fromDate IS NULL OR e.date >= :fromDate) " +
           "AND (:toDate IS NULL OR e.date <= :toDate) " +
           "ORDER BY e.date DESC")
    List<Expense> findFiltered(@Param("username") String username,
                               @Param("status") ExpenseStatus status,
                               @Param("fromDate") LocalDate fromDate,
                               @Param("toDate") LocalDate toDate);

    List<Expense> findByStatusOrderByDateDesc(ExpenseStatus status);

    @Query("SELECT e FROM Expense e WHERE e.status = :status")
    List<Expense> findByStatus(@Param("status") ExpenseStatus status);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.status = :status")
    java.math.BigDecimal sumAmountByStatus(@Param("status") ExpenseStatus status);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.status = :status " +
           "AND e.date >= :fromDate AND e.date <= :toDate")
    java.math.BigDecimal sumAmountByStatusAndDateBetween(@Param("status") ExpenseStatus status,
                                                          @Param("fromDate") LocalDate fromDate,
                                                          @Param("toDate") LocalDate toDate);

    long countByStatus(ExpenseStatus status);

    @Query("SELECT e.category, COALESCE(SUM(e.amount), 0) FROM Expense e " +
           "WHERE e.status = :status GROUP BY e.category")
    List<Object[]> sumAmountByStatusGroupedByCategory(@Param("status") ExpenseStatus status);
}
