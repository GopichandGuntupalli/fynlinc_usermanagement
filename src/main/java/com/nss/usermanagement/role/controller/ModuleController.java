package com.nss.usermanagement.role.controller;

import com.nss.usermanagement.role.constants.UsermangementConstants;
import com.nss.usermanagement.role.request.ModuleRequest;
import com.nss.usermanagement.role.responce.ModuleResponse;
import com.nss.usermanagement.role.service.ModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UsermangementConstants.MODULE_BASE_URL)
@Slf4j
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @PostMapping
    public ResponseEntity<ModuleResponse> createModule(@RequestBody ModuleRequest moduleRequest) {
        log.info("Attempting to create module with request:{}",moduleRequest);
        try{
            ModuleResponse response = moduleService.createModule(moduleRequest);
            log.info("Module created Successfully :{}",response);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch(Exception e){
           log.error("Error created while creating module {}",e.getMessage(),e);
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }


    }

    @GetMapping
    public ResponseEntity<ModuleResponse> getAllModules(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching all modules with pagination - page :{},size:{}",page,size);
        try{
        ModuleResponse response = moduleService.getAllModules(page, size);
        log.info("Fetch all modules successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }catch (Exception e){
            log.error("Error Occurred while fetching all modules:{}",e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(UsermangementConstants.GET_MODULE_BY_ID)
    public ResponseEntity<ModuleResponse> getModuleById(@PathVariable Long id) {
        log.info("Fetching module by ID:{}",id);
        try {
            ModuleResponse response = moduleService.getModuleById(id);
            log.info("Fetching module with ID:{} successfully.",id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            log.error("Error occurred while fetching module by id {} ",e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @PutMapping(UsermangementConstants.UPDATE_MODULE)
    public ResponseEntity<ModuleResponse> updateModule(
            @PathVariable Long id, @RequestBody ModuleRequest moduleRequest) {
        log.info("attempting to update module with ID:{} :{} ", id, moduleRequest);
        try {
            ModuleResponse response = moduleService.updateModule(id, moduleRequest);
            log.info("Module with ID :{} succcessfully",id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while updating module with ID:{}:{}",e.getMessage(),e);
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping(UsermangementConstants.DELETE_MODULE)
    public ResponseEntity<?> deleteModule(@PathVariable Long id) {
        log.info("Attempting to delete module with ID:{}",id);
        try {
            moduleService.deleteModule(id);
            log.info("Module Delete successfully ");
            return ResponseEntity.ok().body("Module deleted successfully.");
        } catch (Exception e) {
            log.error("Error occurred while deleting module with ID:{}",e.getMessage(),e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete module.");
        }
    }

}
