package com.nss.usermanagement.role.responce;

import com.nss.usermanagement.role.model.RolePermissionDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
public class RolePermissionResponse {
    private Page<RolePermissionDTO> rolePermissionPage;
    private int currentPage;
    private long totalItems;
    private int totalPages;

    public RolePermissionResponse(Page<RolePermissionDTO> rolePermissionPage) {
        this.rolePermissionPage = rolePermissionPage;
        this.currentPage = rolePermissionPage.getNumber();
        this.totalItems = rolePermissionPage.getTotalElements();
        this.totalPages = rolePermissionPage.getTotalPages();
    }


    public void setRolePermissions(List<Object> objects) {
    }
}
