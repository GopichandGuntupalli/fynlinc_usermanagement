package com.nss.usermanagement.role.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class AuditLog implements Serializable {
    private Integer status = 1;
    private Date createdOn;
    private Date updatedOn;
    private String createdBy;
    private String updatedBy;
}
