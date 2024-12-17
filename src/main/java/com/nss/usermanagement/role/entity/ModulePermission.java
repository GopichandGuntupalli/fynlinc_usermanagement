package com.nss.usermanagement.role.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "module_permission")
public class ModulePermission extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "module_permission_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "module_permission_operations",
            joinColumns = @JoinColumn(name = "module_permission_id"),
            inverseJoinColumns = @JoinColumn(name = "operation_id")
    )
    @JsonIgnore
    private List<Operation> operations;
}
