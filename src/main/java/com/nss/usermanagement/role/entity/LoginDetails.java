package com.nss.usermanagement.role.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "login_details")
public class LoginDetails extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "TOKEN")
    private String token;
    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;
}
