package chain.trace.zanzibarspice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "farms")
public class Farm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "farmer_id", nullable = false)
    private User farmer;

    @Column(name = "farm_name", nullable = false, length = 150)
    private String farmName;

    @Column(name = "district", nullable = false, length = 100)
    private String district;

    @Column(name = "region", nullable = false, length = 100)
    private String region;

    @Column(name = "land_size_acres", nullable = false)
    private Double landSizeAcres;

    @Column(name = "spice_types", nullable = false)
    private String spiceTypes;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}