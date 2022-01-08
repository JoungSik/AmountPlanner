package com.joung.amount.domain.transaction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TransactionServiceTest {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    @Transactional
    @DisplayName("전체 입출금 내역 리스트")
    void getTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            String date = "2022-01-0" + i;
            int amount = i * 1000;
            String description = "Description - " + i;

            Transaction transaction = Transaction.builder()
                    .data(LocalDate.parse(date, formatter))
                    .amount(amount)
                    .description(description)
                    .build();

            transactions.add(transaction);
        }

        transactionRepository.saveAll(transactions);

        assertEquals(transactionService.getTransactions().size(), transactions.size());
    }

    @Test
    @Transactional
    @DisplayName("입출금 내역 저장")
    void addTransaction() {
        String date = "2022-01-01";
        int amount = 1000;
        String description = "description";

        TransactionDto.Request request = new TransactionDto.Request();
        request.setDate(date);
        request.setAmount(amount);
        request.setDescription(description);

        Transaction result = transactionService.addTransaction(request);

        assertEquals(date, result.getDate().format(formatter));
        assertEquals(amount, result.getAmount());
        assertEquals(description, result.getDescription());
    }

}