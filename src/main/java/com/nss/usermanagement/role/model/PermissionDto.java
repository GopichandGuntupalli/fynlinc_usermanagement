package com.nss.usermanagement.role.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class PermissionDto {
    private Long id;
    private Long module;
    private Long role;
    private List<OperationDTO> selectedOperations;

}

