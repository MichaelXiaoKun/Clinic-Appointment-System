package com.clinic.appointment.clinicappointmentsystem.entity.account.auth;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MINUTES;

@Service
public class LoginAttemptService {

    private final LoadingCache<String, Integer> loginAttemptCache;

    public LoginAttemptService() {
        loginAttemptCache = CacheBuilder
                .newBuilder()
                .expireAfterWrite(15, MINUTES).maximumSize(50)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public void incrementAttempts(String username) {
        int attempts = 0;
        try {
            attempts = loginAttemptCache.get(username) + 1;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            loginAttemptCache.put(username, attempts);
        }
    }

    public void deleteUserFromLoginAttemptCache(String username) {
        loginAttemptCache.invalidate(username);
    }

    public boolean attemptsGreaterThanAllowed(String username) {
        try {
            int MAX_ATTEMPTS_ALLOWED = 5;
            return loginAttemptCache.get(username) >= MAX_ATTEMPTS_ALLOWED;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return false;
    }

}
