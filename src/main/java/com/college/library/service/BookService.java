package com.college.library.service;

import com.college.library.dto.BookDto;
import com.college.library.exception.ResourceNotFoundException;
import com.college.library.model.Book;
import com.college.library.model.TransactionStatus;
import com.college.library.repository.BookRepository;
import com.college.library.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public Book addBook(BookDto dto) {
        if (bookRepository.existsByIsbn(dto.getIsbn())) {
            throw new IllegalStateException("A book with ISBN '" + dto.getIsbn() + "' already exists");
        }
        if (dto.getAvailableCopies() > dto.getTotalCopies()) {
            throw new IllegalStateException("Available copies cannot exceed total copies");
        }

        Book book = Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .isbn(dto.getIsbn())
                .category(dto.getCategory())
                .publisher(dto.getPublisher())
                .publicationYear(dto.getPublicationYear())
                .totalCopies(dto.getTotalCopies())
                .availableCopies(dto.getAvailableCopies())
                .shelfLocation(dto.getShelfLocation())
                .build();

        log.info("Adding new book: {}", book.getTitle());
        return bookRepository.save(book);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
    }

    public Page<Book> searchBooks(String query, String category, Boolean available, Pageable pageable) {
        boolean availableOnly   = Boolean.TRUE.equals(available);
        boolean unavailableOnly = Boolean.FALSE.equals(available);
        return bookRepository.searchBooks(query, category, availableOnly, unavailableOnly, pageable);
    }

    public List<String> getAllCategories() {
        return bookRepository.findAllCategories();
    }

    @Transactional
    public Book updateBook(Long id, BookDto dto) {
        Book book = findById(id);

        if (bookRepository.existsByIsbnAndIdNot(dto.getIsbn(), id)) {
            throw new IllegalStateException("Another book with ISBN '" + dto.getIsbn() + "' already exists");
        }
        if (dto.getAvailableCopies() > dto.getTotalCopies()) {
            throw new IllegalStateException("Available copies cannot exceed total copies");
        }

        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setIsbn(dto.getIsbn());
        book.setCategory(dto.getCategory());
        book.setPublisher(dto.getPublisher());
        book.setPublicationYear(dto.getPublicationYear());
        book.setTotalCopies(dto.getTotalCopies());
        book.setAvailableCopies(dto.getAvailableCopies());
        book.setShelfLocation(dto.getShelfLocation());

        log.info("Updated book: {}", book.getTitle());
        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = findById(id);
        boolean hasActiveIssues = transactionRepository.existsByBookIdAndStatus(id, TransactionStatus.ISSUED);
        if (hasActiveIssues) {
            throw new IllegalStateException("Cannot delete '" + book.getTitle() + "' — it has active issues. Return all copies first.");
        }
        bookRepository.delete(book);
        log.info("Deleted book: {}", book.getTitle());
    }

    public long countTotalBooks() {
        return bookRepository.count();
    }

    public long countAvailableBooks() {
        return bookRepository.countAvailableBooks();
    }

    /** Converts a BookDto to a Book entity (for pre-populating edit forms). */
    public BookDto toDto(Book book) {
        BookDto dto = new BookDto();
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setIsbn(book.getIsbn());
        dto.setCategory(book.getCategory());
        dto.setPublisher(book.getPublisher());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setTotalCopies(book.getTotalCopies());
        dto.setAvailableCopies(book.getAvailableCopies());
        dto.setShelfLocation(book.getShelfLocation());
        return dto;
    }
}
