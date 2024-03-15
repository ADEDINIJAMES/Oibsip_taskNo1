package com.example.atmdemoappforoasis.models;

import com.example.atmdemoappforoasis.enums.TransType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private TransType transactionType;
    @ManyToOne
    private Account accountId;
    @CurrentTimestamp
    private LocalTime transTime;
    @CurrentTimestamp
    private LocalDate transDat;
    @ManyToOne
    private Users owner;
    private String recipientAccNo;
    private BigDecimal amount;
    private String description;

}
