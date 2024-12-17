package com.nss.usermanagement.role.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionDTO {
    private Long id;
    private String role;
    private int status;
    private String description;
    private List<ModulePermissionDTO> modulePermissions;

}
