package com.nss.usermanagement.role.mapper;

import com.nss.usermanagement.role.entity.Operation;
import com.nss.usermanagement.role.model.OperationDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OperationMapper {

    public OperationDTO toDTO(Operation operation) {
        if (operation == null) {
            return null;
        }
        OperationDTO dto = new OperationDTO();
        dto.setId(operation.getId());
        dto.setOperationName(operation.getName());
        return dto;
    }

    public Operation toEntity(OperationDTO dto) {
        if (dto == null) {
            return null;
        }
        Operation operation = new Operation();
        operation.setId(dto.getId());
        operation.setName(dto.getOperationName());

        // Set the audit fields for creation
        operation = prepareForCreation(operation);

        return operation;
    }

    public List<OperationDTO> toDTOList(List<Operation> operations) {
        if (operations == null) {
            return null;
        }
        return operations.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<Operation> toEntityList(List<OperationDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public Operation prepareForCreation(Operation operation) {
        String currentUser = getCurrentUser();
        operation.setCreatedOn(LocalDateTime.now());
        operation.setUpdatedOn(LocalDateTime.now());
        operation.setCreatedBy(currentUser);
        operation.setUpdatedBy(currentUser);
        return operation;
    }

    public Operation prepareForUpdate(Operation operation) {
        String currentUser = getCurrentUser();
        operation.setUpdatedOn(LocalDateTime.now());
        operation.setUpdatedBy(currentUser);
        return operation;
    }

    private String getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
