package com.college.library.service;

import com.college.library.dto.UserRegistrationDto;
import com.college.library.exception.ResourceNotFoundException;
import com.college.library.model.Role;
import com.college.library.model.User;
import com.college.library.repository.TransactionRepository;
import com.college.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerMember(UserRegistrationDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalStateException("Username '" + dto.getUsername() + "' is already taken");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalStateException("Email '" + dto.getEmail() + "' is already registered");
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalStateException("Passwords do not match");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .role(Role.MEMBER)
                .blocked(false)
                .build();

        log.info("Registering new member: {}", user.getUsername());
        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    public Page<User> getAllMembers(Pageable pageable) {
        return userRepository.findByRole(Role.MEMBER, pageable);
    }

    public Page<User> searchMembers(String query, Pageable pageable) {
        return userRepository.searchMembers(query, pageable);
    }

    @Transactional
    public User updateUser(Long id, User updatedData) {
        User user = findById(id);
        user.setFullName(updatedData.getFullName());
        user.setEmail(updatedData.getEmail());
        user.setPhone(updatedData.getPhone());
        user.setAddress(updatedData.getAddress());
        return userRepository.save(user);
    }

    @Transactional
    public void toggleBlockStatus(Long id) {
        User user = findById(id);
        user.setBlocked(!user.isBlocked());
        userRepository.save(user);
        log.info("User {} block status toggled to: {}", user.getUsername(), user.isBlocked());
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findById(id);
        long activeBooks = transactionRepository.countActiveBooksByUser(user);
        if (activeBooks > 0) {
            throw new IllegalStateException("Cannot delete member with " + activeBooks + " active book(s). Please return all books first.");
        }
        userRepository.delete(user);
        log.info("Deleted user: {}", user.getUsername());
    }

    public long countMembers() {
        return userRepository.countByRole(Role.MEMBER);
    }

    @Transactional
    public void changePassword(Long id, String newPassword) {
        User user = findById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
