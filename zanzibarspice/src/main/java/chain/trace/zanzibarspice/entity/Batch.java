package chain.trace.zanzibarspice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "batches")
public class Batch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference_code", nullable = false, unique = true, length = 20)
    private String referenceCode;

    @ManyToOne
    @JoinColumn(name = "farm_id", nullable = false)
    private Farm farm;

    @Column(name = "spice_type", nullable = false, length = 100)
    private String spiceType;

    @Column(name = "quantity_kg", nullable = false)
    private Double quantityKg;

    @Column(name = "harvest_date", nullable = false)
    private LocalDate harvestDate;

    @Column(name = "current_status", nullable = false, length = 30)
    private String currentStatus = "HARVESTED";

    @Column(name = "qr_code_path", length = 255)
    private String qrCodePath;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}