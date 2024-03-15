package com.example.atmdemoappforoasis.service;

import com.example.atmdemoappforoasis.dto.AccountCreationDto;

public interface AccountService {
    String createAccount (AccountCreationDto accountDto);
    String getBalance ();
}
