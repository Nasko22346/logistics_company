package org.informatics.logistics_company.service;

import jakarta.transaction.Transactional;
import org.informatics.logistics_company.dto.staff.StaffUserForm;
import org.informatics.logistics_company.model.jpa.*;
import org.informatics.logistics_company.repository.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class StaffUserAdminService {

    private final StaffRepository staffRepository;
    private final OfficeRepository officeRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final LoginDetailsRepository loginDetailsRepository;
    private final PasswordEncoder passwordEncoder;

    public StaffUserAdminService(
            StaffRepository staffRepository,
            OfficeRepository officeRepository,
            UserDetailsRepository userDetailsRepository,
            LoginDetailsRepository loginDetailsRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.staffRepository = staffRepository;
        this.officeRepository = officeRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.loginDetailsRepository = loginDetailsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Staff> list(String q) {
        if (q == null || q.isBlank()) return staffRepository.findAll();
        return staffRepository.search(q.trim());
    }

    public Staff getById(Long staffId) {
        return staffRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Staff with id " + staffId + " not found"));
    }

    @Transactional
    public void create(StaffUserForm form) {
        if (form.getPassword() == null || form.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password е задължителна при създаване.");
        }

        // ако имаш уникален индекс в DB върху login_email -> това ще го подсили UX-а
        if (loginDetailsRepository.existsByEmailIgnoreCase(form.getEmail())) {
            throw new IllegalArgumentException("Вече има потребител с този email.");
        }

        Office office = officeRepository.findById(form.getOfficeId())
                .orElseThrow(() -> new RuntimeException("Office with id " + form.getOfficeId() + " not found"));

        LoginDetails li = new LoginDetails();
        li.setEmail(form.getEmail());
        li.setPassword(passwordEncoder.encode(form.getPassword()));
        li = loginDetailsRepository.save(li);

        UserDetails ui = new UserDetails();
        ui.setFirstName(form.getFirstName());
        ui.setMiddleName(form.getMiddleName());
        ui.setLastName(form.getLastName());
        ui.setPhoneNumber(form.getPhoneNumber());
        ui.setLoginDetails(li);
        ui = userDetailsRepository.save(ui);

        Staff staff = new Staff();
        staff.setOffice(office);
        staff.setPosition(form.getPosition());
        staff.setStaffUserDetails(ui);

        try {
            staffRepository.save(staff);
        } catch (DataIntegrityViolationException ex) {
            // fallback за DB constraints
            throw new IllegalArgumentException("Неуспешно създаване. Провери данните (възможно дублиране на email).");
        }
    }

    @Transactional
    public void update(Long staffId, StaffUserForm form) {
        Staff staff = getById(staffId);
        if (staff.getStaffUserDetails() == null || staff.getStaffUserDetails().getLoginDetails() == null) {
            throw new RuntimeException("Staff record is invalid (missing user/login info)");
        }

        Office office = officeRepository.findById(form.getOfficeId())
                .orElseThrow(() -> new RuntimeException("Office with id " + form.getOfficeId() + " not found"));

        staff.setOffice(office);
        staff.setPosition(form.getPosition());

        UserDetails ui = staff.getStaffUserDetails();
        ui.setFirstName(form.getFirstName());
        ui.setMiddleName(form.getMiddleName());
        ui.setLastName(form.getLastName());
        ui.setPhoneNumber(form.getPhoneNumber());

        LoginDetails li = ui.getLoginDetails();

        // ако сменяш email и вече съществува за друг -> гръмни с nice message
        String newEmail = form.getEmail();
        if (newEmail != null && !newEmail.equalsIgnoreCase(li.getEmail())) {
            if (loginDetailsRepository.existsByEmailIgnoreCase(newEmail)) {
                throw new IllegalArgumentException("Вече има потребител с този email.");
            }
            li.setEmail(newEmail);
        }

        // password: ако е празно -> не сменяй
        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            li.setPassword(form.getPassword());
        }

        try {
            loginDetailsRepository.save(li);
            userDetailsRepository.save(ui);
            staffRepository.save(staff);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Неуспешна редакция. Провери данните (възможно дублиране на email).");
        }
    }

    @Transactional
    public void delete(Long staffId) {
        Staff staff = getById(staffId);
        UserDetails ui = staff.getStaffUserDetails();
        LoginDetails li = (ui != null ? ui.getLoginDetails() : null);

        staffRepository.delete(staff);
        if (ui != null) userDetailsRepository.delete(ui);
        if (li != null) loginDetailsRepository.delete(li);
    }
}
