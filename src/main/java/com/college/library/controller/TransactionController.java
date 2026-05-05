package com.college.library.controller;

import com.college.library.dto.IssueBookDto;
import com.college.library.model.Book;
import com.college.library.model.Transaction;
import com.college.library.model.User;
import com.college.library.service.BookService;
import com.college.library.service.TransactionService;
import com.college.library.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final BookService bookService;

    @GetMapping("/transactions")
    public String transactionsList(@RequestParam(defaultValue = "0") int page, Model model) {
        PageRequest pageable = PageRequest.of(page, 15, Sort.by("id").descending());
        Page<Transaction> transactions = transactionService.getAllTransactions(pageable);
        model.addAttribute("transactions", transactions);
        model.addAttribute("currentPage", page);
        return "admin/transactions";
    }

    @GetMapping("/issue-book")
    public String issueBookForm(Model model) {
        model.addAttribute("issueDto", new IssueBookDto());
        model.addAttribute("members", userService.getAllMembers(PageRequest.of(0, 100)).getContent());
        model.addAttribute("books", bookService.searchBooks(null, null, true,
                PageRequest.of(0, 200)).getContent());
        return "admin/issue-book";
    }

    @PostMapping("/issue-book")
    public String issueBook(@Valid @ModelAttribute("issueDto") IssueBookDto dto,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("members", userService.getAllMembers(PageRequest.of(0, 100)).getContent());
            model.addAttribute("books", bookService.searchBooks(null, null, true,
                    PageRequest.of(0, 200)).getContent());
            return "admin/issue-book";
        }
        try {
            Transaction t = transactionService.issueBook(dto.getUserId(), dto.getBookId());
            User user = t.getUser();
            Book book = t.getBook();
            redirectAttributes.addFlashAttribute("successMsg",
                    "Book '" + book.getTitle() + "' issued to " + user.getFullName()
                    + ". Due date: " + t.getDueDate());
            return "redirect:/admin/transactions";
        } catch (IllegalStateException e) {
            model.addAttribute("errorMsg", e.getMessage());
            model.addAttribute("members", userService.getAllMembers(PageRequest.of(0, 100)).getContent());
            model.addAttribute("books", bookService.searchBooks(null, null, true,
                    PageRequest.of(0, 200)).getContent());
            return "admin/issue-book";
        }
    }

    @GetMapping("/transactions/{id}/return")
    public String returnBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Transaction t = transactionService.returnBook(id);
            String msg = "Book returned successfully.";
            if (t.getFineAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
                msg += " Fine of ₹" + t.getFineAmount() + " has been recorded.";
            }
            redirectAttributes.addFlashAttribute("successMsg", msg);
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/transactions";
    }

    @GetMapping("/transactions/{id}/pay-fine")
    public String markFinePaid(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            transactionService.markFinePaid(id);
            redirectAttributes.addFlashAttribute("successMsg", "Fine marked as paid.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/transactions";
    }
}
