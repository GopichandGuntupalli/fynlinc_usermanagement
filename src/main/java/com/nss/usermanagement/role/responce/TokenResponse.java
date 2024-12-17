package com.nss.usermanagement.role.responce;


import com.nss.usermanagement.role.model.ModulePermissionDTO;
import com.nss.usermanagement.role.model.RolePermissionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    private String token;
    private String refreshToken;
    private String email;
    private String username;
    private String fullName;
    private  Long subscriptionId;
    private  Long employeeId;
    private List<RolePermissionSummary> rolePermissionSummaries;
    private List<ModulePermissionDTO> modules;



}
