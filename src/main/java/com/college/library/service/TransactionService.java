package com.college.library.service;

import com.college.library.exception.ResourceNotFoundException;
import com.college.library.model.*;
import com.college.library.repository.BookRepository;
import com.college.library.repository.TransactionRepository;
import com.college.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private static final int LOAN_DAYS = 14;
    private static final BigDecimal FINE_PER_DAY = new BigDecimal("5.00");
    private static final int MAX_BORROW_LIMIT = 3;
    private static final BigDecimal AUTO_BLOCK_FINE_THRESHOLD = new BigDecimal("100.00");

    private final TransactionRepository transactionRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional
    public Transaction issueBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));

        // Validations
        if (user.isBlocked()) {
            throw new IllegalStateException("Member '" + user.getFullName() + "' is blocked and cannot borrow books.");
        }
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No available copies of '" + book.getTitle() + "'.");
        }
        long activeCount = transactionRepository.countActiveBooksByUser(user);
        if (activeCount >= MAX_BORROW_LIMIT) {
            throw new IllegalStateException("Member '" + user.getFullName() + "' has reached the maximum borrow limit of " + MAX_BORROW_LIMIT + " books.");
        }

        // Check unpaid fines — auto-block if threshold exceeded
        BigDecimal unpaidFine = transactionRepository.totalUnpaidFineByUser(user);
        if (unpaidFine.compareTo(AUTO_BLOCK_FINE_THRESHOLD) > 0) {
            user.setBlocked(true);
            userRepository.save(user);
            throw new IllegalStateException("Member has been auto-blocked due to unpaid fines of ₹" + unpaidFine + ". Please clear dues first.");
        }

        LocalDate today = LocalDate.now();
        Transaction transaction = Transaction.builder()
                .user(user)
                .book(book)
                .issueDate(today)
                .dueDate(today.plusDays(LOAN_DAYS))
                .status(TransactionStatus.ISSUED)
                .fineAmount(BigDecimal.ZERO)
                .finePaid(false)
                .build();

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        log.info("Book '{}' issued to user '{}'", book.getTitle(), user.getUsername());
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction returnBook(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionId));

        if (transaction.getStatus() == TransactionStatus.RETURNED) {
            throw new IllegalStateException("This book has already been returned.");
        }

        LocalDate today = LocalDate.now();
        transaction.setReturnDate(today);
        transaction.setStatus(TransactionStatus.RETURNED);

        // Calculate fine if overdue
        if (today.isAfter(transaction.getDueDate())) {
            long daysOverdue = ChronoUnit.DAYS.between(transaction.getDueDate(), today);
            BigDecimal fine = FINE_PER_DAY.multiply(BigDecimal.valueOf(daysOverdue));
            transaction.setFineAmount(fine);
            log.info("Fine of ₹{} calculated for {} days overdue", fine, daysOverdue);
        }

        // Return the copy
        Book book = transaction.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        log.info("Book '{}' returned by user '{}'", book.getTitle(), transaction.getUser().getUsername());
        return transactionRepository.save(transaction);
    }

    @Transactional
    public void markFinePaid(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionId));
        transaction.setFinePaid(true);
        transactionRepository.save(transaction);

        // Un-block user if all fines are cleared
        User user = transaction.getUser();
        BigDecimal remaining = transactionRepository.totalUnpaidFineByUser(user);
        if (remaining.compareTo(AUTO_BLOCK_FINE_THRESHOLD) <= 0 && user.isBlocked()) {
            user.setBlocked(false);
            userRepository.save(user);
            log.info("User '{}' auto-unblocked after fine payment", user.getUsername());
        }
    }

    public Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", id));
    }

    public List<Transaction> getActiveTransactionsByUser(User user) {
        return transactionRepository.findByUserAndStatus(user, TransactionStatus.ISSUED);
    }

    public List<Transaction> getAllTransactionsByUser(User user) {
        return transactionRepository.findByUser(user);
    }

    public Page<Transaction> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    public Page<Transaction> getTransactionsByUser(User user, Pageable pageable) {
        return transactionRepository.findByUser(user, pageable);
    }

    /** Refresh overdue statuses — mark ISSUED past due-date as OVERDUE. */
    @Transactional
    public void updateOverdueStatuses() {
        List<Transaction> overdue = transactionRepository.findOverdueTransactions(LocalDate.now());
        overdue.forEach(t -> {
            t.setStatus(TransactionStatus.OVERDUE);
            transactionRepository.save(t);
        });
    }

    public BigDecimal getUnpaidFineByUser(User user) {
        return transactionRepository.totalUnpaidFineByUser(user);
    }

    // Dashboard statistics
    public long countIssuedToday() {
        return transactionRepository.countIssuedToday(LocalDate.now());
    }

    public long countOverdueBooks() {
        return transactionRepository.countOverdueBooks(LocalDate.now());
    }

    public BigDecimal getTotalFineCollected() {
        return transactionRepository.totalFineCollected();
    }

    public List<Transaction> getRecentTransactions() {
        return transactionRepository.findTop10ByOrderByIdDesc();
    }
}
