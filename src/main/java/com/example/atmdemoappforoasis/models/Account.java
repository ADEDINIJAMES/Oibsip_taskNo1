package com.example.atmdemoappforoasis.models;

import com.example.atmdemoappforoasis.enums.AccountType;
import com.example.atmdemoappforoasis.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @OneToOne
    private Users user;
    private AccountType accountType;
    @CurrentTimestamp
    private LocalDateTime openingDate;
    private Status status;
    private BigDecimal balance;
    private LocalDateTime modified;
    @Column(unique = true)
    private String accountNo;
    private String accountName;


}
