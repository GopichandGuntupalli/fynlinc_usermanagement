package com.nss.usermanagement.role.request;

import com.nss.usermanagement.role.model.AuditLog;
import com.nss.usermanagement.role.model.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
@NoArgsConstructor
public class UserRequest extends AuditLog {
    private UserDTO userDTO;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private String phoneNumber;
    private List<Long> rolePermissions;
    private String companyName;
    private Integer status;
    private String description;
    private  Long subscriptionId;
    private  Long employeeId;
    private Integer pageSize;
    private Integer page;
    private Integer totalCount;
    private String sortBy;
    private Sort.Direction direction;
    private  String searchKeyword;
}
