package com.example.atmdemoappforoasis.dto;

import com.example.atmdemoappforoasis.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreationDto {
    private AccountType accountType;
}
