package com.nss.usermanagement.role.mapper;

import com.nss.usermanagement.role.entity.Module;
import com.nss.usermanagement.role.model.ModuleDTO;
import com.nss.usermanagement.role.request.ModuleRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleMapper {

    public static ModuleDTO toDTO(Module module) {
        if (module == null) {
            return null;
        }

        ModuleDTO moduleDTO = new ModuleDTO();
        moduleDTO.setModuleId(module.getModuleId());
        moduleDTO.setModuleName(module.getModuleName());
        moduleDTO.setShortName(module.getShortName());
        moduleDTO.setParentModuleId(module.getParentModuleId());

        if (module.getChildModules() != null) {
            List<ModuleDTO> childModulesDTO = module.getChildModules().stream()
                    .map(ModuleMapper::toDTO)
                    .collect(Collectors.toList());
            moduleDTO.setChildModules(childModulesDTO);
        }

        return moduleDTO;
    }

    public static Module toEntity(ModuleRequest moduleRequest) {
        if (moduleRequest == null) {
            return null;
        }

        ModuleDTO dto = moduleRequest.getModuleDTO();
        Module module = new Module();
        module.setModuleId(dto.getModuleId());
        module.setModuleName(dto.getModuleName());
        module.setShortName(dto.getShortName());
        module.setParentModuleId(dto.getParentModuleId());

        // Set the audit fields for creation
     //   module = prepareForCreation(module);

        return module;
    }

    public static Module prepareForCreation(Module module) {
        String currentUser = getCurrentUser();
        module.setCreatedOn(LocalDateTime.now());
        module.setUpdatedOn(LocalDateTime.now());
        module.setCreatedBy(currentUser);
        module.setUpdatedBy(currentUser);
        return module;
    }

    public static Module prepareForUpdate(Module module) {
        String currentUser = getCurrentUser();
        module.setUpdatedOn(LocalDateTime.now());
        module.setUpdatedBy(currentUser);
        return module;
    }

    private static String getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
