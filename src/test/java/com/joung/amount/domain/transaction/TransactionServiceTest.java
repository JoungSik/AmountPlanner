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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

        User sampleUser = userRepository.save(User.builder()
                .email("example2@example.com")
                .password(new BCryptPasswordEncoder().encode("qwer1234"))
                .build());

        List<Transaction> transactions = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Transaction transaction;
            if (i % 2 == 0) {
                transaction = Transaction.builder()
                        .user(sampleUser)
                        .data(LocalDate.parse("2021-02-0" + i, formatter))
                        .amount(1000 * i)
                        .description("description - " + i)
                        .build();
            } else {
                transaction = Transaction.builder()
                        .user(user)
                        .data(LocalDate.parse("2021-01-0" + i, formatter))
                        .amount(1000 * i)
                        .description("description - " + i)
                        .build();
            }
            transactions.add(transaction);
        }
        transactionRepository.saveAll(transactions);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("?????? ????????? ?????? ?????????")
    void getTransactions() {
        LocalDate today = LocalDate.now();

        List<Transaction> transactions = transactionRepository.findAll()
                .stream()
                .filter(transaction -> Objects.equals(user.getId(), transaction.getUser().getId()) &&
                        Objects.equals(transaction.getDate().getYear(), today.getYear()))
                .collect(Collectors.toList());

        assertEquals(transactionService.getTransactions(user.getEmail(), today.format(formatter)).size(), transactions.size());
    }

    @Test
    @Transactional
    @DisplayName("?????? ????????? ?????? ????????? - ??? ??????")
    void getTransactionsWithMonth() {
        LocalDate today = LocalDate.now();

        List<Transaction> transactions = transactionRepository.findAll()
                .stream()
                .filter(transaction -> Objects.equals(user.getId(), transaction.getUser().getId()) &&
                        Objects.equals(transaction.getDate().getYear(), today.getYear()) &&
                        Objects.equals(transaction.getDate().getMonth(), today.getMonth()))
                .collect(Collectors.toList());

        assertEquals(transactionService.getTransactions(user.getEmail(), today.format(formatter)).size(), transactions.size());
    }

    @Test
    @Transactional
    @DisplayName("????????? ?????? ??????")
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