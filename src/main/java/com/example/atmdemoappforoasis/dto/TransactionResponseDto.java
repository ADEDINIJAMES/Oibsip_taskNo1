package com.example.atmdemoappforoasis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDto {
    private Page<TransactionDto> transactionDtoList;
    private int totalNo;
}
