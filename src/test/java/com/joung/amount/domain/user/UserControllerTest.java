package com.joung.amount.domain.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.joung.amount.security.JwtProperties.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, UserRepository userRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
    }

    @Test
    @Transactional
    @DisplayName("POST /users")
    void createUser() throws Exception {
        String email = "example@example.com";
        String password = "qwer1234";

        UserDto.Request request = new UserDto.Request();
        request.setEmail(email);
        request.setPassword(password);

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email));
    }

    @Test
    @Transactional
    @DisplayName("POST /login")
    void login() throws Exception {
        String token = TOKEN_PREFIX + createToken("example@example.com");

        String email = "example@example.com";
        String password = "qwer1234";

        userRepository.save(User.builder()
                .email(email)
                .password(new BCryptPasswordEncoder().encode(password))
                .build());

        UserDto.Request request = new UserDto.Request();
        request.setEmail(email);
        request.setPassword(password);

        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().stringValues(HEADER_STRING, token));
    }

}