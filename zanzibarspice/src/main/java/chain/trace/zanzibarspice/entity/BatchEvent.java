package chain.trace.zanzibarspice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "batch_events")
public class BatchEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "actor_id", nullable = false)
    private User actor;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "event_date")
    private LocalDateTime eventDate = LocalDateTime.now();
}