package chain.trace.zanzibarspice.repository;

import chain.trace.zanzibarspice.entity.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    List<ContactMessage> findAllByOrderBySentAtDesc();
    List<ContactMessage> findByIsReadFalse();
    long countByIsReadFalse();
}