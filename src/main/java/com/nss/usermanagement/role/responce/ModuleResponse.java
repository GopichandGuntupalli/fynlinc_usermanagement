package com.nss.usermanagement.role.responce;

import com.nss.usermanagement.role.model.ModuleDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
@NoArgsConstructor

@Data
public class ModuleResponse {
    private Page<ModuleDTO> modulePage;
    private int currentPage;
    private int totalItems;
    private int totalPages;

    public ModuleResponse(Page<ModuleDTO> modulePage) {
        this.modulePage = modulePage;
        this.currentPage = modulePage.getNumber();
        this.totalItems = modulePage.getNumberOfElements();
        this.totalPages = modulePage.getTotalPages();
    }
}
