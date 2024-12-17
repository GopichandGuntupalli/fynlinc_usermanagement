package com.nss.usermanagement.role.request;

import com.nss.usermanagement.role.model.AuditLog;
import com.nss.usermanagement.role.model.OperationDTO;
import lombok.Data;

@Data
public class OperationRequest extends AuditLog {
    private OperationDTO operationDTO = new OperationDTO();
}
