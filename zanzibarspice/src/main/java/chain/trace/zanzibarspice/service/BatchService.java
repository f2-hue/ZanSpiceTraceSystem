package chain.trace.zanzibarspice.service;

import chain.trace.zanzibarspice.entity.Batch;
import chain.trace.zanzibarspice.entity.BatchEvent;
import chain.trace.zanzibarspice.entity.Farm;
import chain.trace.zanzibarspice.entity.User;
import chain.trace.zanzibarspice.repository.BatchEventRepository;
import chain.trace.zanzibarspice.repository.BatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final BatchRepository batchRepository;
    private final BatchEventRepository batchEventRepository;

    private String generateReferenceCode() {
        int year = Year.now().getValue();
        String prefix = "SCZ-" + year + "-";
        long count = batchRepository.countByReferenceCodeStartingWith(prefix);
        return String.format("%s%05d", prefix, count + 1);
    }

    public Batch createBatch(Farm farm, String spiceType,
                             Double quantityKg, LocalDate harvestDate,
                             User farmer) {
        Batch batch = new Batch();
        batch.setReferenceCode(generateReferenceCode());
        batch.setFarm(farm);
        batch.setSpiceType(spiceType);
        batch.setQuantityKg(quantityKg);
        batch.setHarvestDate(harvestDate);
        batch.setCurrentStatus("HARVESTED");

        Batch saved = batchRepository.save(batch);

        BatchEvent event = new BatchEvent();
        event.setBatch(saved);
        event.setActor(farmer);
        event.setEventType("HARVEST");
        event.setNotes("Batch created at harvest stage");
        event.setEventDate(LocalDateTime.now());
        batchEventRepository.save(event);

        return saved;
    }

    public Batch updateStatus(Long batchId, String newStatus,
                              String notes, User actor) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        batch.setCurrentStatus(newStatus);
        Batch updated = batchRepository.save(batch);

        BatchEvent event = new BatchEvent();
        event.setBatch(updated);
        event.setActor(actor);
        event.setEventType(newStatus);
        event.setNotes(notes);
        event.setEventDate(LocalDateTime.now());
        batchEventRepository.save(event);

        return updated;
    }

    public Optional<Batch> findByReferenceCode(String referenceCode) {
        return batchRepository.findByReferenceCode(referenceCode);
    }

    public Optional<Batch> findById(Long id) {
        return batchRepository.findById(id);
    }

    public List<Batch> getBatchesByFarmer(User farmer,
                                          FarmService farmService) {
        List<Farm> farms = farmService.getFarmsByFarmer(farmer);
        return batchRepository.findByFarmIn(farms);
    }

    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    public List<BatchEvent> getEventsByBatch(Batch batch) {
        return batchEventRepository.findByBatchOrderByEventDateAsc(batch);
    }

    public void flagBatch(Long batchId, User admin) {
        updateStatus(batchId, "FLAGGED",
                "Flagged by administrator", admin);
    }

    public void unflagBatch(Long batchId, User admin) {
        updateStatus(batchId, "DISPATCHED",
                "Unflagged by administrator", admin);
    }
}