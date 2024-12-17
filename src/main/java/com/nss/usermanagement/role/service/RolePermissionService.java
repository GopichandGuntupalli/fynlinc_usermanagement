package com.nss.usermanagement.role.service;

import com.nss.usermanagement.role.entity.ModulePermission;
import com.nss.usermanagement.role.entity.RolePermission;
import com.nss.usermanagement.role.entity.Operation;
import com.nss.usermanagement.role.exception.ResourceNotFoundException;
import com.nss.usermanagement.role.mapper.RolePermissionMapper;
import com.nss.usermanagement.role.mapper.ModulePermissionMapper;
import com.nss.usermanagement.role.model.RolePermissionDTO;
import com.nss.usermanagement.role.request.RolePermissionRequest;
import com.nss.usermanagement.role.responce.RolePermissionResponse;
import com.nss.usermanagement.role.repository.RolePermissionRepository;
import com.nss.usermanagement.role.repository.ModulePermissionRepo;
import com.nss.usermanagement.role.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RolePermissionService {

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private ModulePermissionRepo modulePermissionRepo;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private ModulePermissionMapper modulePermissionMapper;

    @Autowired
    private OperationRepository operationRepository;

    public RolePermissionDTO createRolePermission(RolePermissionRequest rolePermissionRequest) {
        RolePermission rolePermission = rolePermissionMapper.toEntity(rolePermissionRequest);
        List<ModulePermission> modulePermissions = rolePermission.getModulePermissions().stream()
                .map(modulePermission -> {
                    // Ensure all operations are attached to the session
                    List<Operation> attachedOperations = modulePermission.getOperations().stream()
                            .map(operation -> operationRepository.findById(operation.getId())
                                    .orElseThrow(() -> new IllegalArgumentException("Operation with ID " + operation.getId() + " not found")))
                            .collect(Collectors.toList());
                    modulePermission.setOperations(attachedOperations);
                    return modulePermissionRepo.save(modulePermission);
                })
                .collect(Collectors.toList());
        rolePermission.setModulePermissions(modulePermissions);
        RolePermission savedRolePermission = rolePermissionRepository.save(rolePermission);
        return rolePermissionMapper.toDTO(savedRolePermission);
    }
    
    public RolePermissionDTO getRolePermissionById(Long id) {
        RolePermission rolePermission = rolePermissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RolePermission not found with ID " + id));
        return rolePermissionMapper.toDTO(rolePermission);
    }

    public RolePermissionResponse getAllRolePermissions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<RolePermission> rolePermissionsPage = rolePermissionRepository.findAll(pageable);

        // Map RolePermission to RolePermissionDTO
        Page<RolePermissionDTO> rolePermissionDTOPage = rolePermissionsPage.map(rolePermissionMapper::toDTO);

        // Return a RolePermissionResponse with the DTOs
        return new RolePermissionResponse(rolePermissionDTOPage);
    }

    public RolePermissionDTO updateRolePermission(Long id, RolePermissionRequest rolePermissionRequest) {
        Optional<RolePermission> optionalExistingRolePermission = rolePermissionRepository.findById(id);

        if (optionalExistingRolePermission.isEmpty()) {
            throw new RuntimeException("RolePermission not found with id: " + id);
        }

        RolePermission existingRolePermission = optionalExistingRolePermission.get();
         RolePermission rolePermission = rolePermissionMapper.toEntity(rolePermissionRequest);

        RolePermission updatedRolePermission = rolePermissionMapper.updateEntity(existingRolePermission, rolePermission);

        RolePermission savedRolePermission = rolePermissionRepository.save(updatedRolePermission);
        return rolePermissionMapper.toDTO(savedRolePermission);
    }

    public void deleteRolePermission(Long id) {
        if (rolePermissionRepository.existsById(id)) {
            rolePermissionRepository.deleteById(id);
        } else {
            throw new RuntimeException("RolePermission with ID " + id + " does not exist");
        }
    }
}
