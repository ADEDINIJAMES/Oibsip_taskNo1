package com.example.atmdemoappforoasis.repository;

import com.example.atmdemoappforoasis.models.Account;
import com.example.atmdemoappforoasis.models.Transactions;
import com.example.atmdemoappforoasis.models.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transactions, Long> {
    @Override
    Page<Transactions> findAll(Pageable pageable);
        @Query("SELECT t FROM Transactions t WHERE t.owner = :owner AND t.transDat BETWEEN :startDate AND :endDate")
        Page<Transactions> findByOwnerAndTransDateBetween(@Param("owner") Users owner, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);
       Page<Transactions> findByAccountIdAndTransDatBetween(Account accountId, LocalDate transDat, LocalDate transDat2, Pageable pageable);


}
