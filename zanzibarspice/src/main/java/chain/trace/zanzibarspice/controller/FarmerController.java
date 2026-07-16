package chain.trace.zanzibarspice.controller;

import chain.trace.zanzibarspice.entity.Batch;
import chain.trace.zanzibarspice.entity.Farm;
import chain.trace.zanzibarspice.entity.User;
import chain.trace.zanzibarspice.repository.BatchRepository;
import chain.trace.zanzibarspice.service.BatchService;
import chain.trace.zanzibarspice.service.FarmService;
import chain.trace.zanzibarspice.service.QRCodeService;
import chain.trace.zanzibarspice.service.UserService;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/farmer")
@RequiredArgsConstructor
public class FarmerController {

    private final UserService userService;
    private final FarmService farmService;
    private final BatchService batchService;
    private final QRCodeService qrCodeService;
    private final BatchRepository batchRepository;

    // Dashboard
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails,
                            Model model) {
        User farmer = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        List<Farm> farms = farmService.getFarmsByFarmer(farmer);
        List<Batch> batches = batchService.getBatchesByFarmer(farmer, farmService);

        model.addAttribute("farmer", farmer);
        model.addAttribute("farms", farms);
        model.addAttribute("batches", batches);
        return "farmer/dashboard";
    }

    // Show register farm form
    @GetMapping("/farm/register")
    public String registerFarmForm(Model model) {
        model.addAttribute("districts", new String[]{
                "Mjini Magharibi", "Kaskazini A", "Kaskazini B",
                "Kusini", "Kati", "Micheweni", "Wete", "Chake Chake", "Mkoani"
        });
        model.addAttribute("regions", new String[]{"Unguja", "Pemba"});
        model.addAttribute("spiceOptions", new String[]{
                "Cloves", "Cinnamon", "Black Pepper",
                "Cardamom", "Nutmeg", "Turmeric"
        });
        return "farmer/register-farm";
    }

    // Process register farm
    @PostMapping("/farm/register")
    public String processRegisterFarm(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String farmName,
            @RequestParam String district,
            @RequestParam String region,
            @RequestParam Double landSizeAcres,
            @RequestParam String spiceTypes,
            Model model) {

        User farmer = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        farmService.registerFarm(farmer, farmName, district, region,
                landSizeAcres, spiceTypes);
        return "redirect:/farmer/dashboard?farmRegistered=true";
    }

    // Show create batch form
    @GetMapping("/batch/create")
    public String createBatchForm(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        User farmer = userService.findByEmail(
                userDetails.getUsername()).orElseThrow();
        List<Farm> farms = farmService.getFarmsByFarmer(farmer);
        model.addAttribute("farms", farms);
        return "farmer/create-batch";
    }

    // Process create batch
    @PostMapping("/batch/create")
    public String processCreateBatch(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long farmId,
            @RequestParam String spiceType,
            @RequestParam Double quantityKg,
            @RequestParam String harvestDate,
            Model model) {

        User farmer = userService.findByEmail(userDetails.getUsername()).orElseThrow();
        Farm farm = farmService.findById(farmId).orElseThrow();

        Batch batch = batchService.createBatch(
                farm, spiceType, quantityKg,
                LocalDate.parse(harvestDate), farmer);

        // Generate QR code
        String qrPath = qrCodeService.generateQRCode(
                batch.getReferenceCode());
        batch.setQrCodePath(qrPath);
        batchRepository.save(batch);

        return "redirect:/farmer/batch/" + batch.getId();
    }

    // View batch detail
    @GetMapping("/batch/{id}")
    public String viewBatch(@PathVariable Long id, Model model) {
        Batch batch = batchService.findById(id).orElseThrow();
        List events = batchService.getEventsByBatch(batch);
        model.addAttribute("batch", batch);
        model.addAttribute("events", events);
        return "farmer/batch-detail";
    }
    // Get spice types for a specific farm (AJAX)
    @GetMapping("/farm/{id}/spices")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getFarmSpices(
            @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        farmService.findById(id).ifPresent(farm -> {
            response.put("spiceTypes", farm.getSpiceTypes());
        });
        return ResponseEntity.ok(response);
    }
}