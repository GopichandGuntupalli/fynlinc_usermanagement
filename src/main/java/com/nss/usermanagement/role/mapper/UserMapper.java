package com.nss.usermanagement.role.mapper;

import com.nss.usermanagement.role.entity.RolePermission;
import com.nss.usermanagement.role.entity.User;
import com.nss.usermanagement.role.model.RolePermissionDTO;
import com.nss.usermanagement.role.model.UserDTO;
import com.nss.usermanagement.role.repository.RolePermissionRepository;
import com.nss.usermanagement.role.request.UserRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class UserMapper {

    private final RolePermissionRepository rolePermissionRepository;
    private final ModulePermissionMapper modulePermissionMapper;

    public UserMapper(RolePermissionRepository rolePermissionRepository, ModulePermissionMapper modulePermissionMapper) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.modulePermissionMapper = modulePermissionMapper;
    }

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUserName(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setCompanyName(user.getCompanyName());
        dto.setStatus(user.getStatus());
        dto.setDescription(user.getDescription());
        dto.setSubscriptionId(user.getSubscriptionId());
        dto.setEmployeeId(user.getEmployeeId());
        // Deserialize RolePermission IDs and fetch RolePermission details
        if (user.getRolePermissionIds() != null && !user.getRolePermissionIds().isEmpty()) {
            List<Long> roleIds = Stream.of(user.getRolePermissionIds().split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            dto.setRolePermissions(roleIds);

            List<RolePermission> rolePermissions = rolePermissionRepository.findAllById(roleIds);
            dto.setRoleDetails(rolePermissions.stream()
                    .map(rp -> new RolePermissionDTO(
                            rp.getId(),
                            rp.getRole(),
                            rp.getStatus(),
                            rp.getDescription(),
                            modulePermissionMapper.toDTOList(rp.getModulePermissions())
                    ))
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public User toEntity(UserRequest userRequest) {
        if (userRequest == null) {
            return null;
        }

        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setUsername(userRequest.getUserName());
        user.setPassword(userRequest.getPassword());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setCompanyName(userRequest.getCompanyName());
        user.setStatus(userRequest.getStatus());
        user.setDescription(userRequest.getDescription());
        user.setSubscriptionId(userRequest.getSubscriptionId());
        user.setEmployeeId(userRequest.getEmployeeId());

        // Serialize RolePermission IDs to a single column
        if (userRequest.getRolePermissions() != null && !userRequest.getRolePermissions().isEmpty()) {
            String rolePermissionIds = userRequest.getRolePermissions().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            user.setRolePermissionIds(rolePermissionIds);
        }

     //   user = prepareForCreation(user);

        return user;
    }

    public void updateUserEntity(User existingUser, UserRequest userRequest) {
        // Update first name if provided
        if (userRequest.getFirstName() != null && !userRequest.getFirstName().isBlank()) {
            existingUser.setFirstName(userRequest.getFirstName());
        }

        // Update last name if provided
        if (userRequest.getLastName() != null && !userRequest.getLastName().isBlank()) {
            existingUser.setLastName(userRequest.getLastName());
        }

        // Update username if provided
        if (userRequest.getUserName() != null && !userRequest.getUserName().isBlank()) {
            existingUser.setUsername(userRequest.getUserName());
        }

        // Update password if provided
        if (userRequest.getPassword() != null && !userRequest.getPassword().isBlank()) {
            existingUser.setPassword(userRequest.getPassword());

        }

        // Update email if provided
        if (userRequest.getEmail() != null && !userRequest.getEmail().isBlank()) {
            existingUser.setEmail(userRequest.getEmail());
        }

        // Update phone number if provided
        if (userRequest.getPhoneNumber() != null && !userRequest.getPhoneNumber().isBlank()) {
            existingUser.setPhoneNumber(userRequest.getPhoneNumber());
        }

        // Update company name if provided
        if (userRequest.getCompanyName() != null && !userRequest.getCompanyName().isBlank()) {
            existingUser.setCompanyName(userRequest.getCompanyName());
        }
        if(userRequest.getEmployeeId() != null){
            existingUser.setEmployeeId(userRequest.getEmployeeId());
            System.out.println("employeeId: " + userRequest.getEmployeeId());
        }
        if(userRequest.getSubscriptionId() != null){
            existingUser.setSubscriptionId(userRequest.getSubscriptionId());
            System.out.println("subscriptionId: " + userRequest.getSubscriptionId());
        }

        // Update status if provided
        if (userRequest.getStatus() != null) {
            existingUser.setStatus(userRequest.getStatus());
        }

        // Update description if provided
        if (userRequest.getDescription() != null && !userRequest.getDescription().isBlank()) {
            existingUser.setDescription(userRequest.getDescription());
        }

        // Update RolePermission IDs if provided
        if (userRequest.getRolePermissions() != null && !userRequest.getRolePermissions().isEmpty()) {
            String rolePermissionIds = userRequest.getRolePermissions().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            existingUser.setRolePermissionIds(rolePermissionIds);
        }
    }

    private User prepareForCreation(User user) {
        String currentUser = getCurrentUser();
        user.setCreatedOn(LocalDateTime.now());
        user.setUpdatedOn(LocalDateTime.now());
        user.setCreatedBy(currentUser);
        user.setUpdatedBy(currentUser);
        return user;
    }

    private User prepareForUpdate(User user) {
        String currentUser = getCurrentUser();
        user.setUpdatedOn(LocalDateTime.now());
        user.setUpdatedBy(currentUser);
        return user;
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
