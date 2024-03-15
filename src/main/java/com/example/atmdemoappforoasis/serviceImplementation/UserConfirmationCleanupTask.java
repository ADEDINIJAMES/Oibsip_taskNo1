package com.example.atmdemoappforoasis.serviceImplementation;

import com.example.atmdemoappforoasis.models.Users;
import com.example.atmdemoappforoasis.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserConfirmationCleanupTask {
    private final UserRepository userRepository;

    public UserConfirmationCleanupTask(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000) // Run once every 24 hours
    public void deleteExpiredUserConfirmations() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Users> expiredUsers = userRepository.findByConfirmationExpirationBefore(currentTime);
        userRepository.deleteAll(expiredUsers);
    }

}
