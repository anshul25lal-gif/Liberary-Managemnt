package com.college.library.controller;

import com.college.library.dto.BookDto;
import com.college.library.model.Book;
import com.college.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // ─── Catalog (accessible by both roles) ─────────────────────────────────

    private static final java.util.Set<String> ALLOWED_SORT_FIELDS =
            java.util.Set.of("title", "author", "category", "createdAt");

    @GetMapping("/catalog")
    public String catalog(@RequestParam(defaultValue = "") String search,
                          @RequestParam(defaultValue = "") String category,
                          @RequestParam(defaultValue = "") String available,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "title") String sort,
                          Model model) {
        // Safely convert available string → Boolean (empty = no filter)
        Boolean availableBool = available.isEmpty() ? null : Boolean.parseBoolean(available);

        // Guard against arbitrary sort field injection
        String safeSort = ALLOWED_SORT_FIELDS.contains(sort) ? sort : "title";
        Sort sortOrder = Sort.by(safeSort).ascending();
        PageRequest pageable = PageRequest.of(page, 10, sortOrder);

        Page<Book> books = bookService.searchBooks(
                search.isBlank() ? null : search,
                category.isBlank() ? null : category,
                availableBool,
                pageable);

        model.addAttribute("books", books);
        model.addAttribute("categories", bookService.getAllCategories());
        model.addAttribute("search", search);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("available", available);
        model.addAttribute("sort", safeSort);
        model.addAttribute("currentPage", page);
        return "catalog";
    }

    // ─── Admin Book CRUD ─────────────────────────────────────────────────────

    @GetMapping("/admin/books")
    @PreAuthorize("hasRole('ADMIN')")
    public String booksList(@RequestParam(defaultValue = "") String search,
                            @RequestParam(defaultValue = "") String category,
                            @RequestParam(defaultValue = "0") int page,
                            Model model) {
        PageRequest pageable = PageRequest.of(page, 10, Sort.by("title").ascending());
        Page<Book> books = bookService.searchBooks(
                search.isBlank() ? null : search,
                category.isBlank() ? null : category,
                null,
                pageable);

        model.addAttribute("books", books);
        model.addAttribute("categories", bookService.getAllCategories());
        model.addAttribute("search", search);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("currentPage", page);
        return "admin/books-list";
    }

    @GetMapping("/admin/books/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String newBookForm(Model model) {
        model.addAttribute("bookDto", new BookDto());
        model.addAttribute("categories", bookService.getAllCategories());
        model.addAttribute("isEdit", false);
        return "admin/book-form";
    }

    @PostMapping("/admin/books/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveBook(@Valid @ModelAttribute("bookDto") BookDto dto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", bookService.getAllCategories());
            model.addAttribute("isEdit", false);
            return "admin/book-form";
        }
        try {
            bookService.addBook(dto);
            redirectAttributes.addFlashAttribute("successMsg", "Book added successfully!");
            return "redirect:/admin/books";
        } catch (IllegalStateException e) {
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("categories", bookService.getAllCategories());
            model.addAttribute("isEdit", false);
            return "admin/book-form";
        }
    }

    @GetMapping("/admin/books/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editBookForm(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id);
        model.addAttribute("bookDto", bookService.toDto(book));
        model.addAttribute("bookId", id);
        model.addAttribute("categories", bookService.getAllCategories());
        model.addAttribute("isEdit", true);
        return "admin/book-form";
    }

    @PostMapping("/admin/books/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateBook(@PathVariable Long id,
                             @Valid @ModelAttribute("bookDto") BookDto dto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("bookId", id);
            model.addAttribute("categories", bookService.getAllCategories());
            model.addAttribute("isEdit", true);
            return "admin/book-form";
        }
        try {
            bookService.updateBook(id, dto);
            redirectAttributes.addFlashAttribute("successMsg", "Book updated successfully!");
            return "redirect:/admin/books";
        } catch (IllegalStateException e) {
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("bookId", id);
            model.addAttribute("categories", bookService.getAllCategories());
            model.addAttribute("isEdit", true);
            return "admin/book-form";
        }
    }

    @GetMapping("/admin/books/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteBook(id);
            redirectAttributes.addFlashAttribute("successMsg", "Book deleted successfully.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/books";
    }
}
