package chain.trace.zanzibarspice.repository;

import chain.trace.zanzibarspice.entity.Batch;
import chain.trace.zanzibarspice.entity.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
    Optional<Batch> findByReferenceCode(String referenceCode);
    List<Batch> findByFarm(Farm farm);
    List<Batch> findByFarmIn(List<Farm> farms);
    long countByReferenceCodeStartingWith(String prefix);
}