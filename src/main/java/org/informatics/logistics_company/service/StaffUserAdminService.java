package org.informatics.logistics_company.service;

import jakarta.transaction.Transactional;
import org.informatics.logistics_company.dto.staff.StaffUserForm;
import org.informatics.logistics_company.model.jpa.*;
import org.informatics.logistics_company.repository.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffUserAdminService {

    private final StaffRepository staffRepository;
    private final OfficeRepository officeRepository;
    private final UserInfoRepository userInfoRepository;
    private final LoginInfoRepository loginInfoRepository;

    public StaffUserAdminService(
            StaffRepository staffRepository,
            OfficeRepository officeRepository,
            UserInfoRepository userInfoRepository,
            LoginInfoRepository loginInfoRepository
    ) {
        this.staffRepository = staffRepository;
        this.officeRepository = officeRepository;
        this.userInfoRepository = userInfoRepository;
        this.loginInfoRepository = loginInfoRepository;
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
        if (loginInfoRepository.existsByEmailIgnoreCase(form.getEmail())) {
            throw new IllegalArgumentException("Вече има потребител с този email.");
        }

        Office office = officeRepository.findById(form.getOfficeId())
                .orElseThrow(() -> new RuntimeException("Office with id " + form.getOfficeId() + " not found"));

        LoginInfo li = new LoginInfo();
        li.setEmail(form.getEmail());
        li.setPassword(form.getPassword()); // plain text (временно)
        li = loginInfoRepository.save(li);

        UserInfo ui = new UserInfo();
        ui.setFirstName(form.getFirstName());
        ui.setMiddleName(form.getMiddleName());
        ui.setLastName(form.getLastName());
        ui.setPhoneNumber(form.getPhoneNumber());
        ui.setLoginInfo(li);
        ui = userInfoRepository.save(ui);

        Staff staff = new Staff();
        staff.setOffice(office);
        staff.setPosition(form.getPosition());
        staff.setStaffUserInfo(ui);

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
        if (staff.getStaffUserInfo() == null || staff.getStaffUserInfo().getLoginInfo() == null) {
            throw new RuntimeException("Staff record is invalid (missing user/login info)");
        }

        Office office = officeRepository.findById(form.getOfficeId())
                .orElseThrow(() -> new RuntimeException("Office with id " + form.getOfficeId() + " not found"));

        staff.setOffice(office);
        staff.setPosition(form.getPosition());

        UserInfo ui = staff.getStaffUserInfo();
        ui.setFirstName(form.getFirstName());
        ui.setMiddleName(form.getMiddleName());
        ui.setLastName(form.getLastName());
        ui.setPhoneNumber(form.getPhoneNumber());

        LoginInfo li = ui.getLoginInfo();

        // ако сменяш email и вече съществува за друг -> гръмни с nice message
        String newEmail = form.getEmail();
        if (newEmail != null && !newEmail.equalsIgnoreCase(li.getEmail())) {
            if (loginInfoRepository.existsByEmailIgnoreCase(newEmail)) {
                throw new IllegalArgumentException("Вече има потребител с този email.");
            }
            li.setEmail(newEmail);
        }

        // password: ако е празно -> не сменяй
        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            li.setPassword(form.getPassword());
        }

        try {
            loginInfoRepository.save(li);
            userInfoRepository.save(ui);
            staffRepository.save(staff);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Неуспешна редакция. Провери данните (възможно дублиране на email).");
        }
    }

    @Transactional
    public void delete(Long staffId) {
        Staff staff = getById(staffId);
        UserInfo ui = staff.getStaffUserInfo();
        LoginInfo li = (ui != null ? ui.getLoginInfo() : null);

        staffRepository.delete(staff);
        if (ui != null) userInfoRepository.delete(ui);
        if (li != null) loginInfoRepository.delete(li);
    }
}
