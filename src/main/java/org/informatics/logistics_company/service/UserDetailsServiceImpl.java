package org.informatics.logistics_company.service;

import org.informatics.logistics_company.model.jpa.LoginDetails;
import org.informatics.logistics_company.repository.LoginDetailsRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final LoginDetailsRepository loginRepository;

    public UserDetailsServiceImpl(LoginDetailsRepository userRepository) {
        this.loginRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LoginDetails user = loginRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String roleName = user.getRole() != null ? user.getRole().name() : "USER";

        return User.withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(roleName)
                .build();
    }
}
