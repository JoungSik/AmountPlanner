package com.joung.amount.domain.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findTransactionByUserId(Long userId);

    List<Transaction> findTransactionByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);

}
