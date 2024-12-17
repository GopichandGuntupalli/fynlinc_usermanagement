package com.nss.usermanagement.role.constants;

public class UsermangementConstants {
    public static final String BASE_URL="/admin/api";
    public static final String LOGIN="/auth/login";
    public static final String FORGOT_PASSWORD="auth/forgotPassword";
    public static final String GENERATE_TOKEN="auth/generateToken";
    public static final String VALIDATE_TOKEN="auth/validateToken";

    public static final String LOGOUT= "/auth/logout";
    public static final String  USER_BASE_URL= BASE_URL+"/users";
    public static final String GET_USER_BY_ID="/{id}";

    public static final String GET_ALL_USERS="/getAllUsers";
    public static final String UPDATE_USER="/{id}";
    public static final String DELETE_USER="/{id}";

    public static final String ROLE_PERMISSION_BASE_URL= BASE_URL+"/rolePermissions";
    public static final String GET_ROLE_PERMISSION_BY_ID="/{id}";

    public static final String GET_ALL_ROLE_PERMISSIONS="/getAllRolePermissions";
    public static final String UPDATE_ROLE_PERMISSION="/{id}";
    public static final String DELETE_ROLE_PERMISSION="/{id}";

    public static final String OPERATION_BASE_URL= BASE_URL+"/operations";
    public static final String GET_OPERATION_BY_ID="/{id}";
    public static final String UPDATE_OPERATION="/{id}";
    public static final String DELETE_OPERATION="/{id}";

    public static final String MODULE_BASE_URL= BASE_URL+"/modules";
    public static final String GET_MODULE_BY_ID="/{id}";
    public static final String UPDATE_MODULE="/{id}";
    public static final String DELETE_MODULE="/{id}";






}
