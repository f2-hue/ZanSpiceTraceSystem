package chain.trace.zanzibarspice.controller;

import chain.trace.zanzibarspice.entity.Batch;
import chain.trace.zanzibarspice.entity.BatchEvent;
import chain.trace.zanzibarspice.entity.User;
import chain.trace.zanzibarspice.service.BatchService;
import chain.trace.zanzibarspice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/trader")
@RequiredArgsConstructor
public class TraderController {

    private final UserService userService;
    private final BatchService batchService;

    // Trader dashboard
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails,
                            Model model) {
        User trader = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        List<Batch> allBatches = batchService.getAllBatches();
        model.addAttribute("trader", trader);
        model.addAttribute("batches", allBatches);
        return "trader/dashboard";
    }

    // Search batch by reference code
    @GetMapping("/batch/search")
    public String searchBatch(@RequestParam(required = false) String referenceCode,
                              Model model) {
        if (referenceCode != null && !referenceCode.isEmpty()) {
            Optional<Batch> batch = batchService.findByReferenceCode(referenceCode);
            if (batch.isPresent()) {
                model.addAttribute("batch", batch.get());
                List<BatchEvent> events = batchService.getEventsByBatch(batch.get());
                model.addAttribute("events", events);
            } else {
                model.addAttribute("error", "No batch found with reference: " + referenceCode);
            }
        }
        return "trader/search-batch";
    }

    // Show update status form
    @GetMapping("/batch/{id}/update")
    public String updateStatusForm(@PathVariable Long id, Model model) {
        Batch batch = batchService.findById(id).orElseThrow();
        model.addAttribute("batch", batch);
        model.addAttribute("statuses", new String[]{
                "IN_PROCESSING", "PACKAGED", "DISPATCHED"
        });
        return "trader/update-status";
    }

    // Process update status
    @PostMapping("/batch/{id}/update")
    public String processUpdateStatus(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String newStatus,
            @RequestParam(required = false) String notes,
            Model model) {

        User trader = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        batchService.updateStatus(id, newStatus, notes, trader);
        return "redirect:/trader/dashboard?updated=true";
    }

    // View batch detail
    @GetMapping("/batch/{id}")
    public String viewBatch(@PathVariable Long id, Model model) {
        Batch batch = batchService.findById(id).orElseThrow();
        List<BatchEvent> events = batchService.getEventsByBatch(batch);
        model.addAttribute("batch", batch);
        model.addAttribute("events", events);
        return "trader/batch-detail";
    }
}