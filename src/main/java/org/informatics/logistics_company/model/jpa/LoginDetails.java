package org.informatics.logistics_company.model.jpa;

import jakarta.persistence.*;
import lombok.Data;
import org.informatics.logistics_company.model.enums.Role;

@Data
@Table(name = "login_info")
@Entity
public class LoginDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "login_id")
    private Long id;

    @Column(name = "login_email")
    private String email;

    @Column(name = "login_password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role = Role.USER;
}
