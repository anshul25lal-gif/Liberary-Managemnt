package com.college.library.controller;

import com.college.library.model.User;
import com.college.library.service.BookService;
import com.college.library.service.TransactionService;
import com.college.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final BookService bookService;
    private final TransactionService transactionService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Refresh overdue statuses on every dashboard load
        transactionService.updateOverdueStatuses();

        model.addAttribute("totalBooks", bookService.countTotalBooks());
        model.addAttribute("totalMembers", userService.countMembers());
        model.addAttribute("issuedToday", transactionService.countIssuedToday());
        model.addAttribute("overdueBooks", transactionService.countOverdueBooks());
        model.addAttribute("fineCollected", transactionService.getTotalFineCollected());
        model.addAttribute("recentTransactions", transactionService.getRecentTransactions());
        return "admin/dashboard";
    }

    // ─── Member Management ──────────────────────────────────────────────────

    @GetMapping("/members")
    public String membersList(@RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "0") int page,
                              Model model) {
        PageRequest pageable = PageRequest.of(page, 10, Sort.by("fullName").ascending());
        Page<User> members = search.isBlank()
                ? userService.getAllMembers(pageable)
                : userService.searchMembers(search, pageable);

        model.addAttribute("members", members);
        model.addAttribute("search", search);
        model.addAttribute("currentPage", page);
        return "admin/members-list";
    }

    @GetMapping("/members/{id}/toggle-block")
    public String toggleBlock(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.toggleBlockStatus(id);
            redirectAttributes.addFlashAttribute("successMsg", "Member block status updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/members";
    }

    @GetMapping("/members/{id}/delete")
    public String deleteMember(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMsg", "Member deleted successfully.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/members";
    }

    @GetMapping("/members/{id}")
    public String viewMember(@PathVariable Long id, Model model) {
        User member = userService.findById(id);
        model.addAttribute("member", member);
        model.addAttribute("activeBooks", transactionService.getActiveTransactionsByUser(member));
        model.addAttribute("allTransactions", transactionService.getAllTransactionsByUser(member));
        model.addAttribute("unpaidFine", transactionService.getUnpaidFineByUser(member));
        return "admin/member-detail";
    }
}
