package org.informatics.logistics_company.service;

import org.informatics.logistics_company.dto.client.ClientForm;
import org.informatics.logistics_company.model.enums.Role;
import org.informatics.logistics_company.model.jpa.LoginDetails;
import org.informatics.logistics_company.model.jpa.UserDetails;
import org.informatics.logistics_company.repository.LoginDetailsRepository;
import org.informatics.logistics_company.repository.StaffRepository;
import org.informatics.logistics_company.repository.UserDetailsRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientService {

    private final UserDetailsRepository userDetailsRepository;
    private final StaffRepository staffRepository;
    private final LoginDetailsRepository loginDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientService(UserDetailsRepository userDetailsRepository,
                         StaffRepository staffRepository,
                         LoginDetailsRepository loginDetailsRepository,
                         PasswordEncoder passwordEncoder) {
        this.userDetailsRepository = userDetailsRepository;
        this.staffRepository = staffRepository;
        this.loginDetailsRepository = loginDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDetails> listClients(String q) {
        String query = (q == null) ? "" : q.trim();
        return query.isBlank()
                ? userDetailsRepository.findAllClients()
                : userDetailsRepository.searchClients(query);
    }

    public ClientForm getClientForm(Long id) {
        UserDetails u = userDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client with id " + id + " not found"));

        // гаранция, че не е staff
        if (staffRepository.existsByStaffUserDetails_Id(id)) {
            throw new RuntimeException("User with id " + id + " is staff, not a client");
        }

        LoginDetails login = u.getLoginDetails();
        return new ClientForm(
                u.getId(),
                u.getFirstName(),
                u.getMiddleName(),
                u.getLastName(),
                u.getPhoneNumber(),
                login != null ? login.getEmail() : null,
                null,
                login != null ? login.getRole() : Role.USER
        );
    }

    @Transactional
    public void create(ClientForm form) {
        UserDetails u = new UserDetails();
        applyBasic(u, form);

        LoginDetails login = buildOrNullLogin(null, form);

        u.setLoginDetails(login);

        if (login != null) {
            login.setPassword(passwordEncoder.encode(form.getLoginPassword()));
            loginDetailsRepository.save(login);
        }
        userDetailsRepository.save(u);
    }

    @Transactional
    public void update(Long id, ClientForm form) {
        UserDetails u = userDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client with id " + id + " not found"));

        if (staffRepository.existsByStaffUserDetails_Id(id)) {
            throw new RuntimeException("User with id " + id + " is staff, not a client");
        }

        applyBasic(u, form);

        LoginDetails current = u.getLoginDetails();
        LoginDetails updated = buildOrNullLogin(current, form);

        if (updated == null && current != null) {
            u.setLoginDetails(null);
            userDetailsRepository.save(u);
            loginDetailsRepository.delete(current);
            return;
        }

        if (updated != null) {
            updated.setPassword(passwordEncoder.encode(form.getLoginPassword()));
            LoginDetails saved = loginDetailsRepository.save(updated);
            u.setLoginDetails(saved);
        }

        userDetailsRepository.save(u);
    }

    @Transactional
    public void delete(Long id) {
        UserDetails u = userDetailsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client with id " + id + " not found"));

        if (staffRepository.existsByStaffUserDetails_Id(id)) {
            throw new RuntimeException("User with id " + id + " is staff, not a client");
        }

        LoginDetails login = u.getLoginDetails();
        try {
            userDetailsRepository.delete(u);
            if (login != null) {
                loginDetailsRepository.delete(login);
            }
        } catch (DataIntegrityViolationException e) {
            // например ако има FK към parcels
            throw new RuntimeException("Cannot delete client because it is referenced by other records (e.g. parcels).");
        }
    }

    private void applyBasic(UserDetails u, ClientForm form) {
        u.setFirstName(form.getFirstName());
        u.setMiddleName(form.getMiddleName());
        u.setLastName(form.getLastName());
        u.setPhoneNumber(form.getPhoneNumber());
    }


    private LoginDetails buildOrNullLogin(LoginDetails existing, ClientForm form) {
        String email = safe(form.getLoginEmail());
        String pass = safe(form.getLoginPassword());

        boolean wantsLogin = !email.isBlank() || !pass.isBlank() || form.getRole() != null;
        if (!wantsLogin) return null;

        LoginDetails login = (existing != null) ? existing : new LoginDetails();

        if (!email.isBlank()) login.setEmail(email);

        if (!pass.isBlank()) login.setPassword(pass);

        login.setRole(form.getRole() != null ? form.getRole() : Role.USER);
        return login;
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }
}
