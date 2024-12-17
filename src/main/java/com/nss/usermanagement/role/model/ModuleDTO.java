package com.nss.usermanagement.role.model;

import lombok.Data;

import java.util.List;

@Data
public class ModuleDTO {

    private Long moduleId;
    private String moduleName;
    private String shortName;
    private Long parentModuleId;
    private List<ModuleDTO> childModules;
}
