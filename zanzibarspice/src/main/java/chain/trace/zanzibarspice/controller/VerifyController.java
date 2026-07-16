package chain.trace.zanzibarspice.controller;

import chain.trace.zanzibarspice.entity.Batch;
import chain.trace.zanzibarspice.entity.BatchEvent;
import chain.trace.zanzibarspice.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class VerifyController {

    private final BatchService batchService;

    // Public verify page
    @GetMapping("/verify")
    public String verifyPage(@RequestParam(required = false) String ref,
                             Model model) {
        if (ref != null && !ref.isEmpty()) {
            Optional<Batch> batch = batchService.findByReferenceCode(ref);
            if (batch.isPresent()) {
                List<BatchEvent> events = batchService.getEventsByBatch(batch.get());
                model.addAttribute("batch", batch.get());
                model.addAttribute("events", events);
            } else {
                model.addAttribute("error",
                        "No batch found with reference: " + ref);
            }
        }
        return "verify";
    }

    // QR code scan redirects here
    @GetMapping("/verify/{referenceCode}")
    public String verifyByCode(@PathVariable String referenceCode, Model model) {
        Optional<Batch> batch = batchService.findByReferenceCode(referenceCode);
        if (batch.isPresent()) {
            List<BatchEvent> events = batchService.getEventsByBatch(batch.get());
            model.addAttribute("batch", batch.get());
            model.addAttribute("events", events);
        } else {
            model.addAttribute("error",
                    "No batch found with reference: " + referenceCode);
        }
        return "verify";
    }
}