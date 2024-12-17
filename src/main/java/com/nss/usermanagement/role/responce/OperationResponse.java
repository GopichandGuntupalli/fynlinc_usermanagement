package com.nss.usermanagement.role.responce;

import com.nss.usermanagement.role.model.OperationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor

@Data
public class OperationResponse {
    private List<OperationDTO> operationPage;
    private int currentPage;
    private int totalItems;
    private int totalPages;

    public OperationResponse(Page<OperationDTO> operationDTOPage) {
    }

//    public OperationResponse(Page<OperationDTO> operationPage) {
//        this.operationPage = operationPage;
//        this.currentPage = operationPage.getNumber();
//        this.totalItems = operationPage.getNumberOfElements();
//        this.totalPages = operationPage.getTotalPages();
//    }
}
