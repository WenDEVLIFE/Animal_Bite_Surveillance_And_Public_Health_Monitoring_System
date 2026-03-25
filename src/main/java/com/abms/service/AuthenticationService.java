package com.abms.service;

import com.abms.repository.UserRepository;

public class AuthenticationService {
    private final UserRepository userRepository;

    public AuthenticationService() {
        this.userRepository = new UserRepository();
    }

    public boolean login(String username, String password) {
        // Simple login for now, could add hashing later
        return userRepository.authenticate(username, password);
    }

    public String getUserRole(String username) {
        return userRepository.getUserRole(username);
    }
}
