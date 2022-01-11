package com.joung.amount.domain.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.joung.amount.security.JwtProperties.TOKEN_PREFIX;
import static com.joung.amount.security.JwtProperties.createToken;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final TransactionService transactionService;

    private String token;

    @Autowired
    public TransactionControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, TransactionService transactionService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.transactionService = transactionService;
    }

    @BeforeEach
    void setUser() {
        token = createToken("example@example.com");
    }

    @Test
    @DisplayName("GET /transactions")
    void getTransactions() throws Exception {
        List<Transaction> result = transactionService.getTransactions();

        mockMvc.perform(MockMvcRequestBuilders.get("/transactions")
                        .header("Authorization", TOKEN_PREFIX + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(result.size()));
    }

    @Test
    @Transactional
    @DisplayName("POST /transactions")
    void createTransaction() throws Exception {
        String date = "2022-01-01";
        int amount = 1000;
        String description = "description";

        TransactionDto.Request request = new TransactionDto.Request();
        request.setDate(date);
        request.setAmount(amount);
        request.setDescription(description);

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", TOKEN_PREFIX + token))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value(date))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(amount))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(description));
    }

}