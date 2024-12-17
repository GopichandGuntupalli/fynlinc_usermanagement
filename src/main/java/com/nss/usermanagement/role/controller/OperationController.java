package com.nss.usermanagement.role.controller;

import com.nss.usermanagement.role.constants.UsermangementConstants;
import com.nss.usermanagement.role.model.OperationDTO;
import com.nss.usermanagement.role.request.OperationRequest;
import com.nss.usermanagement.role.responce.OperationResponse;
import com.nss.usermanagement.role.service.OperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(UsermangementConstants.OPERATION_BASE_URL)
public class OperationController {

    @Autowired
    private OperationService operationService;

    @PostMapping
    public ResponseEntity<OperationDTO> createOperation(@RequestBody OperationRequest operationRequest) {
        log.info("Attempting to create operation with request:{}", operationRequest);
        try {
            OperationDTO createdOperation = operationService.createOperation(operationRequest.getOperationDTO());
            log.info("Operation created succcessfully:{} ", createdOperation);
            return ResponseEntity.ok(createdOperation);
        } catch (Exception e) {
            log.error("Error created while creating operation:{}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(UsermangementConstants.GET_OPERATION_BY_ID)
    public ResponseEntity<OperationDTO> getOperationById(@PathVariable Long id) {
        log.info("Fetching operation by ID:{}", id);
        try {
            OperationDTO operationDTO = operationService.getOperationById(id);
            log.info("Fetching Operation ID:{} successfully", operationDTO);
            return ResponseEntity.ok(operationDTO);

        } catch (Exception e) {
            log.error("ID creating null please check the id value :{}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        // if (operationDTO == null) {
        //                log.info()
        //                return ResponseEntity.notFound().build();
        //            }
        //            return ResponseEntity.ok(operationDTO);
    }

    @GetMapping
    public ResponseEntity<OperationResponse> getAllOperations(@RequestParam int page, @RequestParam int size) {
        log.info("Fetch all operation by page and size:{}{}", page, size);
        try {
            OperationResponse response = operationService.getAllOperations(page, size);
            log.info("Fetch all operations successfully :{}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("please check the page and size:{}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping(UsermangementConstants.DELETE_OPERATION)
    public ResponseEntity<String> deleteOperation(@PathVariable Long id) {
        try {
            operationService.deleteOperation(id);
            return ResponseEntity.ok("Operation deleted successfully.");
        } catch (Exception e) {
            // Optional: Handle specific exceptions, e.g., if the operation is not found
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete operation with ID " + id);
        }
    }


    @PutMapping(UsermangementConstants.UPDATE_OPERATION)
    public ResponseEntity<OperationDTO> updateOperation(@PathVariable Long id, @RequestBody OperationRequest operationRequest) {
        log.info("attempting to update module with ID:{} {}", id, operationRequest);
        OperationDTO updatedOperation = operationService.updateOperation(id, operationRequest.getOperationDTO());
        if (updatedOperation == null) {
            log.error("Error occurred while updating module with ID:");
            return ResponseEntity.notFound().build();
        }
        log.info("Operation updated successfully by ID:{}", updatedOperation);
        return ResponseEntity.ok(updatedOperation);
    }

}
