package chain.trace.zanzibarspice.repository;

import chain.trace.zanzibarspice.entity.Batch;
import chain.trace.zanzibarspice.entity.BatchEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BatchEventRepository extends JpaRepository<BatchEvent, Long> {
    List<BatchEvent> findByBatchOrderByEventDateAsc(Batch batch);
}