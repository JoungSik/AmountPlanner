package com.joung.amount.domain.transaction;

import com.joung.amount.domain.user.User;
import com.joung.amount.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TransactionRepositoryTest {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private User user;

    @Autowired
    public TransactionRepositoryTest(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
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
                        .data(LocalDate.parse("2022-01-0" + i, formatter))
                        .amount(1000 * i)
                        .description("description - " + i)
                        .build();
            } else {
                transaction = Transaction.builder()
                        .user(user)
                        .data(LocalDate.parse("2022-01-0" + i, formatter))
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
        transactionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("입출금 내역 유저로 검색")
    void findTransactionByUser() {
        List<Transaction> transactions = transactionRepository.findAll()
                .stream()
                .filter(transaction -> Objects.equals(user.getId(), transaction.getUser().getId()))
                .collect(Collectors.toList());

        assertEquals(transactionRepository.findTransactionByUserId(user.getId()).size(), transactions.size());
    }
}