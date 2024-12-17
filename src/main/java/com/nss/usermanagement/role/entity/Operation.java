package com.nss.usermanagement.role.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "operation")
public class Operation extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}
