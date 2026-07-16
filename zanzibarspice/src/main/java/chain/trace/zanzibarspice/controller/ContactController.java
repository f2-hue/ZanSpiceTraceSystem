package chain.trace.zanzibarspice.controller;

import chain.trace.zanzibarspice.service.ContactMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ContactController {

    private final ContactMessageService contactMessageService;

    // Process contact form submission
    @PostMapping("/contact")
    public String submitMessage(
            @RequestParam String senderName,
            @RequestParam String senderEmail,
            @RequestParam String message) {

        contactMessageService.saveMessage(senderName, senderEmail, message);
        return "redirect:/#contact?sent=true";
    }

    // Admin view all messages
    @GetMapping("/admin/messages")
    public String viewMessages(Model model) {
        model.addAttribute("messages",
                contactMessageService.getAllMessages());
        model.addAttribute("unreadCount",
                contactMessageService.countUnread());
        return "admin/messages";
    }

    // Admin mark as read
    @PostMapping("/admin/messages/{id}/read")
    public String markAsRead(@PathVariable Long id) {
        contactMessageService.markAsRead(id);
        return "redirect:/admin/messages";
    }

    // Admin delete message
    @PostMapping("/admin/messages/{id}/delete")
    public String deleteMessage(@PathVariable Long id) {
        contactMessageService.deleteMessage(id);
        return "redirect:/admin/messages?deleted=true";
    }
}