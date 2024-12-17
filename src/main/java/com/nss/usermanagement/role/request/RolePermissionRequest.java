package com.nss.usermanagement.role.request;

import com.nss.usermanagement.role.model.AuditLog;
import com.nss.usermanagement.role.model.RolePermissionDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RolePermissionRequest extends AuditLog {
    private RolePermissionDTO rolePermissionDTO = new RolePermissionDTO();
}
