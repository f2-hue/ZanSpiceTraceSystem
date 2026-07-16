package chain.trace.zanzibarspice.service;

import chain.trace.zanzibarspice.entity.ContactMessage;
import chain.trace.zanzibarspice.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;

    public ContactMessage saveMessage(String senderName,
                                      String senderEmail,
                                      String message) {
        ContactMessage msg = new ContactMessage();
        msg.setSenderName(senderName);
        msg.setSenderEmail(senderEmail);
        msg.setMessage(message);
        msg.setIsRead(false);
        return contactMessageRepository.save(msg);
    }

    public List<ContactMessage> getAllMessages() {
        return contactMessageRepository.findAllByOrderBySentAtDesc();
    }

    public List<ContactMessage> getUnreadMessages() {
        return contactMessageRepository.findByIsReadFalse();
    }

    public long countUnread() {
        return contactMessageRepository.countByIsReadFalse();
    }

    public void markAsRead(Long id) {
        contactMessageRepository.findById(id).ifPresent(msg -> {
            msg.setIsRead(true);
            contactMessageRepository.save(msg);
        });
    }

    public void deleteMessage(Long id) {
        contactMessageRepository.deleteById(id);
    }

    public Optional<ContactMessage> findById(Long id) {
        return contactMessageRepository.findById(id);
    }
}