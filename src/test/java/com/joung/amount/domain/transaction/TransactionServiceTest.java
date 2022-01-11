package com.joung.amount.domain.transaction;

import com.joung.amount.domain.user.User;
import com.joung.amount.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TransactionServiceTest {

    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    private User user;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public TransactionServiceTest(TransactionService transactionService, TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUser() {
        user = userRepository.save(User.builder()
                .email("example@example.com")
                .password(new BCryptPasswordEncoder().encode("qwer1234"))
                .build());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("전체 입출금 내역 리스트")
    void getTransactions() {
        List<Transaction> transactions = transactionRepository.findAll()
                .stream()
                .filter(transaction -> Objects.equals(user.getId(), transaction.getUser().getId()))
                .collect(Collectors.toList());

        assertEquals(transactionService.getTransactions().size(), transactions.size());
    }

    @Test
    @Transactional
    @DisplayName("입출금 내역 저장")
    void addTransaction() {
        Long userId = user.getId();
        String date = "2022-01-01";
        int amount = 1000;
        String description = "description";

        TransactionDto.Request request = new TransactionDto.Request();
        request.setDate(date);
        request.setAmount(amount);
        request.setDescription(description);

        Transaction result = transactionService.addTransaction(user.getEmail(), request);

        assertEquals(userId, result.getUser().getId());
        assertEquals(date, result.getDate().format(formatter));
        assertEquals(amount, result.getAmount());
        assertEquals(description, result.getDescription());
    }

}