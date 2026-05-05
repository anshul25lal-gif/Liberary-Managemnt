package com.college.library.repository;

import com.college.library.model.Transaction;
import com.college.library.model.TransactionStatus;
import com.college.library.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserAndStatus(User user, TransactionStatus status);

    List<Transaction> findByUser(User user);

    Page<Transaction> findByUser(User user, Pageable pageable);

    long countByStatus(TransactionStatus status);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.issueDate = :today")
    long countIssuedToday(@Param("today") LocalDate today);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.status = 'ISSUED' AND t.dueDate < :today")
    long countOverdueBooks(@Param("today") LocalDate today);

    @Query("SELECT COALESCE(SUM(t.fineAmount), 0) FROM Transaction t WHERE t.finePaid = true")
    BigDecimal totalFineCollected();

    @Query("SELECT COALESCE(SUM(t.fineAmount), 0) FROM Transaction t WHERE t.user = :user AND t.finePaid = false AND t.fineAmount > 0")
    BigDecimal totalUnpaidFineByUser(@Param("user") User user);

    @Query("SELECT t FROM Transaction t WHERE t.status = 'ISSUED' AND t.dueDate < :today")
    List<Transaction> findOverdueTransactions(@Param("today") LocalDate today);

    boolean existsByBookIdAndStatus(Long bookId, TransactionStatus status);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.user = :user AND t.status = 'ISSUED'")
    long countActiveBooksByUser(@Param("user") User user);

    List<Transaction> findTop10ByOrderByIdDesc();
}
