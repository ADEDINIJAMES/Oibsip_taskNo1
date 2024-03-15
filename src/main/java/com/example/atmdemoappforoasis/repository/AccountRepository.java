package com.example.atmdemoappforoasis.repository;

import com.example.atmdemoappforoasis.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
   Optional<Account> findAccountByAccountNo(String acctNo);
}
