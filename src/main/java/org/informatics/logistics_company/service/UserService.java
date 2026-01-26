package org.informatics.logistics_company.service;

import jakarta.transaction.Transactional;
import org.informatics.logistics_company.exception.EmailAlreadyExistsException;
import org.informatics.logistics_company.model.jpa.LoginDetails;
import org.informatics.logistics_company.model.jpa.UserDetails;
import org.informatics.logistics_company.repository.LoginDetailsRepository;
import org.informatics.logistics_company.repository.UserDetailsRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final LoginDetailsRepository loginRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsRepository userDetailsRepository;

    public UserService(LoginDetailsRepository loginRepository, UserDetailsRepository userDetailsRepository,
                       PasswordEncoder passwordEncoder) {
        this.loginRepository = loginRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(String firstName, String middleName, String lastName, String phoneNumber, String email, String rawPassword) {
        if (loginRepository.existsByEmailIgnoreCase(email))
            throw new EmailAlreadyExistsException("Email already exists");

        LoginDetails loginDetails = new LoginDetails();
        loginDetails.setEmail(email);
        loginDetails.setPassword(passwordEncoder.encode(rawPassword));

        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(firstName);
        userDetails.setMiddleName(middleName);
        userDetails.setLastName(lastName);
        userDetails.setPhoneNumber(phoneNumber);
        userDetails.setLoginDetails(loginDetails);

        loginRepository.save(loginDetails);
        userDetailsRepository.save(userDetails);
    }

    public void login(String email, String rawPassword) {
        LoginDetails user = loginRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }
}

