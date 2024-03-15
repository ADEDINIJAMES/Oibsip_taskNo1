package com.example.atmdemoappforoasis.dto;

import com.example.atmdemoappforoasis.enums.TransType;
import com.example.atmdemoappforoasis.models.Account;
import com.example.atmdemoappforoasis.models.Users;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private TransType transactionType;
    private String recipientAccountNo;
    private BigDecimal amount;
    private String description;
    private String accountNo;
    private LocalTime transTime;
    private LocalDate transDat;
}
