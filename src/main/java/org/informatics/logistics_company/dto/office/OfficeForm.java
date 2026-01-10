package org.informatics.logistics_company.dto.office;

public class OfficeForm {
    private Long id;
    private String officePhone;
    private String officeEmail;
    private Long companyId;

    public OfficeForm() {}

    public OfficeForm(Long id, String officePhone, String officeEmail, Long companyId) {
        this.id = id;
        this.officePhone = officePhone;
        this.officeEmail = officeEmail;
        this.companyId = companyId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOfficePhone() { return officePhone; }
    public void setOfficePhone(String officePhone) { this.officePhone = officePhone; }

    public String getOfficeEmail() { return officeEmail; }
    public void setOfficeEmail(String officeEmail) { this.officeEmail = officeEmail; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
}
