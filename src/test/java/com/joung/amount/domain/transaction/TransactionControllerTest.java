package com.joung.amount.domain.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joung.amount.domain.user.User;
import com.joung.amount.domain.user.UserRepository;
import com.joung.amount.security.JwtProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final TransactionService transactionService;
    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;

    private String token;

    @Autowired
    public TransactionControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, TransactionService transactionService, UserRepository userRepository, JwtProperties jwtProperties) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.transactionService = transactionService;
        this.userRepository = userRepository;
        this.jwtProperties = jwtProperties;
    }

    @BeforeEach
    void setUser() {
        String email = "example@example.com";
        token = jwtProperties.createToken(email);
        userRepository.save(User.builder()
                .email(email)
                .password(new BCryptPasswordEncoder().encode("qwer1234"))
                .build());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /api/transactions")
    void getTransactions() throws Exception {
        List<Transaction> result = transactionService.getTransactions("example@example.com");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/transactions")
                        .header("Authorization", jwtProperties.TOKEN_PREFIX + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(result.size()));
    }

    @Test
    @Transactional
    @DisplayName("POST /api/transactions")
    void createTransaction() throws Exception {
        String date = "2022-01-01";
        int amount = 1000;
        String description = "description";

        TransactionDto.Request request = new TransactionDto.Request();
        request.setDate(date);
        request.setAmount(amount);
        request.setDescription(description);

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/transactions")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtProperties.TOKEN_PREFIX + token))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value(date))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(amount))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(description));
    }

}