package com.cancikrikci.receiptprocessor.service;

import com.cancikrikci.receiptprocessor.entity.User;
import com.cancikrikci.receiptprocessor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    
    public Optional<User> findByUsername(String username) {
        log.info("Finding user by username: {}", username);
        return userRepository.findByUsername(username);
    }
    
    public void saveUser(User user) {
        log.info("Saving user: {}", user.getUsername());
        userRepository.save(user);
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}

