package com.example.springboot.controller;

import com.example.springboot.model.BusinessDao;
import com.example.springboot.model.StoreBusinessRequestManual;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class ManuallyChangeBusinessController {
    @Autowired
    private BusinessDao businessDao;
    @PostMapping("/manuallyInsertBusiness")
    public ResponseEntity<String> manualInsert(@RequestBody StoreBusinessRequestManual request) throws SQLException {
        businessDao.saveBusinessManually(request);
        return ResponseEntity.ok().body("successfully add a business manually");
    }

    @DeleteMapping("/manuallyDeleteBusiness")
    public ResponseEntity<String> manualDelete(@RequestBody StoreBusinessRequestManual request) throws SQLException {
        businessDao.deleteBusinessManually(request);
        return ResponseEntity.ok().body("successfully delete a business manually");
    }


}
