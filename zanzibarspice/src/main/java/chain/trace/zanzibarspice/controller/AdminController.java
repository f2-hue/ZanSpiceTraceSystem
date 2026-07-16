package chain.trace.zanzibarspice.controller;

import chain.trace.zanzibarspice.entity.Batch;
import chain.trace.zanzibarspice.entity.User;
import chain.trace.zanzibarspice.service.BatchService;
import chain.trace.zanzibarspice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final BatchService batchService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<User> users = userService.getAllUsers();
        List<Batch> batches = batchService.getAllBatches();
        long pendingCount = users.stream()
                .filter(u -> u.getStatus().equals("PENDING")).count();
        long activeCount = users.stream()
                .filter(u -> u.getStatus().equals("ACTIVE")).count();
        model.addAttribute("users", users);
        model.addAttribute("batches", batches);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("activeCount", activeCount);
        model.addAttribute("totalBatches", batches.size());
        return "admin/dashboard";
    }

    @PostMapping("/users/{id}/approve")
    public String approveUser(@PathVariable Long id) {
        userService.approveUser(id);
        return "redirect:/admin/users?approved=true";
    }

    @PostMapping("/users/{id}/suspend")
    public String suspendUser(@PathVariable Long id) {
        userService.suspendUser(id);
        return "redirect:/admin/users?suspended=true";
    }

    @PostMapping("/users/{id}/activate")
    public String activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return "redirect:/admin/users?activated=true";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users?deleted=true";
    }

    @GetMapping("/users")
    public String viewUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @GetMapping("/batches")
    public String viewBatches(Model model) {
        List<Batch> batches = batchService.getAllBatches();
        model.addAttribute("batches", batches);
        return "admin/batches";
    }

    @PostMapping("/batches/{id}/flag")
    public String flagBatch(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User admin = userService.findByEmail(
                userDetails.getUsername()).orElseThrow();
        batchService.flagBatch(id, admin);
        return "redirect:/admin/batches?flagged=true";
    }

    @PostMapping("/batches/{id}/unflag")
    public String unflagBatch(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User admin = userService.findByEmail(
                userDetails.getUsername()).orElseThrow();
        batchService.unflagBatch(id, admin);
        return "redirect:/admin/batches?unflagged=true";
    }
}