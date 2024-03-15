package com.example.atmdemoappforoasis.controller;

import com.example.atmdemoappforoasis.dto.AccountCreationDto;
import com.example.atmdemoappforoasis.serviceImplementation.AccountServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountServiceImplementation accountServiceImplementation;

@Autowired
    public AccountController(AccountServiceImplementation accountServiceImplementation) {
        this.accountServiceImplementation = accountServiceImplementation;
    }
    @PostMapping("/createAccount")
    public ResponseEntity<String> createAccount (@RequestBody AccountCreationDto accountCreationDto){
        String response = accountServiceImplementation.createAccount(accountCreationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
@GetMapping("/get-details")
    public ResponseEntity<String> getDetails (){
    String response = accountServiceImplementation.getBalance();
    return ResponseEntity.ok(response);
}

}
