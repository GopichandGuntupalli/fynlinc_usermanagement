package com.nss.usermanagement.role.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;



@Entity
@Data
@Table(name = "\"user\"")
public class User extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "First name is required")
  //  @Size(max = 50, message = "First name must not exceed 50 characters")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
  //  @Size(max = 50, message = "Last name must not exceed 50 characters")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Username is required")
   // @Size(max = 50, message = "Username must not exceed 50 characters")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password is required")
   // @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotBlank(message = "Company name is required")
   // @Size(max = 50, message = "Company name must not exceed 100 characters")
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @NotNull(message = "Status is required")
    @Column(name = "status", nullable = false )
    private int status;

   // @Size(max = 255, message = "Description must not exceed 255 characters")
    @Column(name = "description", length = 255)
    private String description;


    @Column(name = "employee_id", length = 20)
    private Long employeeId;

    @Column(name = "subcription_Id", length = 20)
    private Long subscriptionId; ;

    @Column(name = "role_permission_ids", columnDefinition = "TEXT")
    @JsonIgnore
    private String rolePermissionIds; // Serialized list of RolePermission IDs


}
