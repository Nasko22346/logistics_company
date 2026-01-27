package org.informatics.logistics_company.dto.office;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class OfficeForm {
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @NotBlank(message = "Phone number is required")
    private String officePhone;

    @Getter
    @Setter
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String officeEmail;

    @Getter
    @Setter
    @NotNull(message = "Company is required")
    private Long companyId;

    @Getter
    @Setter
    private Long locationId;

    public OfficeForm() {}

    public OfficeForm(Long id, String officePhone, String officeEmail, Long companyId, Long locationId) {
        this.id = id;
        this.officePhone = officePhone;
        this.officeEmail = officeEmail;
        this.companyId = companyId;
        this.locationId = locationId;
    }

//    public String getOfficePhone() { return officePhone; }
//    public void setOfficePhone(String officePhone) { this.officePhone = officePhone; }
//
//    public String getOfficeEmail() { return officeEmail; }
//    public void setOfficeEmail(String officeEmail) { this.officeEmail = officeEmail; }
//
//    public Long getCompanyId() { return companyId; }
//    public void setCompanyId(Long companyId) { this.companyId = companyId; }

}
