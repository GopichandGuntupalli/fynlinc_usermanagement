package com.nss.usermanagement.role.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "role_permission")
public class RolePermission extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Role is required")
    @Column(name = "role", nullable = false)
    private String role;

    @NotNull(message = "Status is required")
    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "description", length = 255)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "role_permission_id")
    @JsonIgnore
    private List<ModulePermission> modulePermissions;
}
