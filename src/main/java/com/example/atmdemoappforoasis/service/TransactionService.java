package com.example.atmdemoappforoasis.service;

import com.example.atmdemoappforoasis.dto.TransactionDto;
import com.example.atmdemoappforoasis.dto.TransactionResponseDto;
import com.example.atmdemoappforoasis.models.Transactions;
import com.example.atmdemoappforoasis.models.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    String doTransaction (TransactionDto transactionDto);
    Page<TransactionDto> getTransaction (LocalDate date1, LocalDate date2, Pageable pageable);
}
