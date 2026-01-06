package org.informatics.logistics_company.model.jpa;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_info")
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "login_id", referencedColumnName = "login_id")
    private LoginDetails loginDetails;
}
