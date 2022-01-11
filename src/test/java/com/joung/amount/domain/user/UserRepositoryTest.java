package com.joung.amount.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeEach
    @Transactional
    void setUp() {
        userRepository.save(User.builder()
                .email("example@example.com")
                .password(new BCryptPasswordEncoder().encode("qwer1234"))
                .build());
    }

    @Test
    @DisplayName("유저 이메일 검색")
    void findUserByEmail() {
        String email = "example@example.com";
        User user = userRepository.findUserByEmail(email);

        assertNotNull(user);
        assertEquals(user.getEmail(), email);
    }
}