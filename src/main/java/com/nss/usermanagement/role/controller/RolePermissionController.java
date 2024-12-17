package com.nss.usermanagement.role.controller;

import com.nss.usermanagement.role.responce.RolePermissionResponse;
import com.nss.usermanagement.role.constants.UsermangementConstants;
import com.nss.usermanagement.role.model.RolePermissionDTO;
import com.nss.usermanagement.role.request.RolePermissionRequest;
import com.nss.usermanagement.role.service.RolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UsermangementConstants.ROLE_PERMISSION_BASE_URL)
@Slf4j
public class RolePermissionController {

    @Autowired
    private RolePermissionService rolePermissionService;


    @PostMapping
    public ResponseEntity<RolePermissionDTO> createRolePermission(@RequestBody RolePermissionRequest rolePermissionRequest) {
        log.info("Request to create role permission with data: {}", rolePermissionRequest);
        RolePermissionDTO createdRolePermission = rolePermissionService.createRolePermission(rolePermissionRequest);
        log.info("Role permission created successfully with ID: {}", createdRolePermission.getId());
        return ResponseEntity.ok(createdRolePermission);
    }

    @GetMapping(UsermangementConstants.GET_ROLE_PERMISSION_BY_ID)
    public ResponseEntity<RolePermissionDTO> getRolePermissionById(@PathVariable Long id) {
        log.info("Fetching role permission with ID: {}", id);
        RolePermissionDTO rolePermissionDTO = rolePermissionService.getRolePermissionById(id);
        if (rolePermissionDTO != null) {
            log.info("Role permission found: {}", rolePermissionDTO);
            return ResponseEntity.ok(rolePermissionDTO);
        } else {
            log.warn("Role permission not found with ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(UsermangementConstants.GET_ALL_ROLE_PERMISSIONS)
    public ResponseEntity<RolePermissionResponse> getAllRolePermissions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Fetching all role permissions with page: {}, size: {}", page, size);
        RolePermissionResponse response = rolePermissionService.getAllRolePermissions(page, size);
        log.info("Fetched {} role permissions", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping(UsermangementConstants.UPDATE_ROLE_PERMISSION)
    public ResponseEntity<RolePermissionDTO> updateRolePermission(
            @PathVariable("id") Long id,
            @RequestBody RolePermissionRequest rolePermissionRequest) {

        log.info("Request to update role permission with ID: {} and data: {}", id, rolePermissionRequest);
        RolePermissionDTO updatedRolePermissionDTO = rolePermissionService.updateRolePermission(id, rolePermissionRequest);

        if (updatedRolePermissionDTO != null) {
            log.info("Role permission updated successfully for ID: {}", id);
            return new ResponseEntity<>(updatedRolePermissionDTO, HttpStatus.OK);
        } else {
            log.warn("Role permission not found for update with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(UsermangementConstants.DELETE_ROLE_PERMISSION)
    public ResponseEntity<String> deleteRolePermission(@PathVariable Long id) {
        log.info("Request to delete role permission with ID: {}", id);
        try {
            rolePermissionService.deleteRolePermission(id);
            log.info("Role permission deleted successfully with ID: {}", id);
            return ResponseEntity.ok("RolePermission deleted successfully.");
        } catch (Exception e) {
            log.error("Error occurred while deleting role permission with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete RolePermission with ID " + id);
        }
    }
}
