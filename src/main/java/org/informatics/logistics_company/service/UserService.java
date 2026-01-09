package org.informatics.logistics_company.service;

import org.informatics.logistics_company.model.jpa.LoginDetails;
import org.informatics.logistics_company.repository.LoginDetailsRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final LoginDetailsRepository loginRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(LoginDetailsRepository loginRepository,
                       PasswordEncoder passwordEncoder) {
        this.loginRepository = loginRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(String email, String rawPassword) {
        if (loginRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        LoginDetails user = new LoginDetails();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));

        loginRepository.save(user);
    }
}

