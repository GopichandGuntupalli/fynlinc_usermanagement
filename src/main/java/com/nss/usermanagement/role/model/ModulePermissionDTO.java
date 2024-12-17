package com.nss.usermanagement.role.model;

import lombok.Data;

import java.util.List;

@Data
public class ModulePermissionDTO {

    private Long id;
    private ModuleDTO module;
    private List<OperationDTO> operations;
    private String operationsAsString;
}
