package org.informatics.logistics_company.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "login_info")
@Entity
public class LoginInfo {

    @Id
    @Column(name = "login_id")
    private Long id;

    @Column(name = "login_email")
    private String email;

    @Column(name = "login_password")
    private String password;
}
