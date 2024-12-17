package com.nss.usermanagement.role.model;

import lombok.Data;
import java.util.List;

@Data
public class UserDTO {
    private Long userId;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String phoneNumber;
    private String companyName;
    private int status;
    private String description;
    private  Long subscriptionId;
    private  Long employeeId;
    private List<Long> rolePermissions; // List of RolePermission IDs
    private List<RolePermissionDTO> roleDetails; // Detailed RolePermission information
}
