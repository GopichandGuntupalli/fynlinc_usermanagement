package com.nss.usermanagement.role.mapper;

import com.nss.usermanagement.role.entity.Module;
import com.nss.usermanagement.role.entity.ModulePermission;
import com.nss.usermanagement.role.entity.Operation;
import com.nss.usermanagement.role.model.ModuleDTO;
import com.nss.usermanagement.role.model.ModulePermissionDTO;
import com.nss.usermanagement.role.model.OperationDTO;
import com.nss.usermanagement.role.repository.ModuleRepository;
import com.nss.usermanagement.role.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ModulePermissionMapper {

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    public ModulePermissionDTO toDTO(ModulePermission modulePermission) {
        if (modulePermission == null) {
            return null;
        }
        ModulePermissionDTO dto = new ModulePermissionDTO();
        dto.setId(modulePermission.getId());

        // Fetch and set the ModuleDTO
        Module module = modulePermission.getModule();
        if (module != null) {
            ModuleDTO moduleDTO = new ModuleDTO();
            moduleDTO.setModuleId(module.getModuleId());
            moduleDTO.setModuleName(module.getModuleName());
            moduleDTO.setParentModuleId(module.getParentModuleId());
            moduleDTO.setShortName(module.getShortName());

            // Convert List<Module> to List<ModuleDTO>
            List<ModuleDTO> childModulesDTO = module.getChildModules().stream()
                    .map(childModule -> {
                        ModuleDTO childModuleDTO = new ModuleDTO();
                        childModuleDTO.setModuleId(childModule.getModuleId());
                        childModuleDTO.setModuleName(childModule.getModuleName());
                        childModuleDTO.setParentModuleId(childModule.getParentModuleId());
                        childModuleDTO.setShortName(childModule.getShortName());
                        return childModuleDTO;
                    })
                    .collect(Collectors.toList());

            moduleDTO.setChildModules(childModulesDTO);
            dto.setModule(moduleDTO);
        }

        // Map the operations
        dto.setOperations(modulePermission.getOperations().stream()
                .map(operation -> {
                    OperationDTO operationDTO = new OperationDTO();
                    operationDTO.setId(operation.getId());
                    operationDTO.setOperationName(operation.getName());
                    return operationDTO;
                }).collect(Collectors.toList()));

        // Set operationsAsString
        dto.setOperationsAsString(modulePermission.getOperations().stream()
                .map(Operation::getName)
                .collect(Collectors.joining(", ")));

        return dto;
    }

    public ModulePermission toEntity(ModulePermissionDTO dto) {
        if (dto == null) {
            return null;
        }
        ModulePermission modulePermission = new ModulePermission();
        modulePermission.setId(dto.getId());

        // Fetch and set the Module entity
        if (dto.getModule() != null && dto.getModule().getModuleId() != null) {
            Module module = moduleRepository.findById(dto.getModule().getModuleId())
                    .orElseThrow(() -> new IllegalArgumentException("Module with ID " + dto.getModule().getModuleId() + " not found"));
            modulePermission.setModule(module);
        }

        // Fetch and set the Operations
        List<Operation> operations = dto.getOperations().stream()
                .map(operationDTO -> operationRepository.findById(operationDTO.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Operation with ID " + operationDTO.getId() + " not found")))
                .collect(Collectors.toList());

        modulePermission.setOperations(operations);

        return modulePermission;
    }

    public List<ModulePermissionDTO> toDTOList(List<ModulePermission> modulePermissions) {
        if (modulePermissions == null) {
            return null;
        }
        return modulePermissions.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ModulePermission> toEntityList(List<ModulePermissionDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
