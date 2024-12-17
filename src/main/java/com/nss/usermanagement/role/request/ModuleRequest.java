package com.nss.usermanagement.role.request;

import com.nss.usermanagement.role.model.AuditLog;
import com.nss.usermanagement.role.model.ModuleDTO;
import lombok.Data;

@Data
public class ModuleRequest extends AuditLog {
    private ModuleDTO moduleDTO = new ModuleDTO();
}
