package com.nss.usermanagement.role.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "module")
public class Module extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "module_id")
    private Long moduleId;

    @NotBlank(message = "Module name is required")
    @Column(name = "module_name", nullable = false,unique = true)
    private String moduleName;

    @Column(name = "short_name")
    private String shortName;

    @NotNull(message = "Parent module ID is required")
    @Column(name = "parent_module_id", nullable = true)
    private Long parentModuleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_module_id", insertable = false, updatable = false)
    private Module parentModule;

    @OneToMany(mappedBy = "parentModule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Module> childModules;

}
