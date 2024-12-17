package com.nss.usermanagement.role.responce;

import com.nss.usermanagement.role.model.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse<T> {
    private List<T> data = new ArrayList<>();
    private int currentPage;
    private int totalItems;
    private int totalPages;
    private  int pageSize;



    public void setUsers(List<Object> objects) {
    }

//    public UserResponse(Page<UserDTO> userPage) {
//        this.userPage = userPage;
//        this.currentPage = userPage.getNumber();
//        this.totalItems = userPage.getNumberOfElements();
//        this.totalPages = userPage.getTotalPages();
//    }
}
