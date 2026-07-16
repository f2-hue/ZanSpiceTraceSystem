package chain.trace.zanzibarspice.service;

import chain.trace.zanzibarspice.entity.User;
import chain.trace.zanzibarspice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(String fullName, String email,
                             String password, String role) {
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(role);
        user.setStatus("PENDING");
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void approveUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setStatus("ACTIVE");
            userRepository.save(user);
        });
    }

    public void suspendUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setStatus("SUSPENDED");
            userRepository.save(user);
        });
    }

    public void activateUser(Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setStatus("ACTIVE");
            userRepository.save(user);
        });
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}