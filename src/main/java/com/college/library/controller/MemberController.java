package com.college.library.controller;

import com.college.library.model.User;
import com.college.library.service.TransactionService;
import com.college.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final UserService userService;
    private final TransactionService transactionService;

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails principal, Model model) {
        User user = userService.findByUsername(principal.getUsername());
        var activeBooks = transactionService.getActiveTransactionsByUser(user);

        // Highlight overdue books for the member
        LocalDate today = LocalDate.now();
        model.addAttribute("user", user);
        model.addAttribute("activeBooks", activeBooks);
        model.addAttribute("today", today);
        model.addAttribute("unpaidFine", transactionService.getUnpaidFineByUser(user));
        return "member/dashboard";
    }

    @GetMapping("/my-books")
    public String myBooks(@AuthenticationPrincipal UserDetails principal, Model model) {
        User user = userService.findByUsername(principal.getUsername());
        LocalDate today = LocalDate.now();
        model.addAttribute("activeBooks", transactionService.getActiveTransactionsByUser(user));
        model.addAttribute("allTransactions", transactionService.getAllTransactionsByUser(user));
        model.addAttribute("today", today);
        model.addAttribute("unpaidFine", transactionService.getUnpaidFineByUser(user));
        return "member/my-books";
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails principal, Model model) {
        User user = userService.findByUsername(principal.getUsername());
        model.addAttribute("user", user);
        return "member/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal UserDetails principal,
                                @ModelAttribute User updatedData,
                                RedirectAttributes redirectAttributes) {
        User user = userService.findByUsername(principal.getUsername());
        try {
            userService.updateUser(user.getId(), updatedData);
            redirectAttributes.addFlashAttribute("successMsg", "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/member/profile";
    }
}
