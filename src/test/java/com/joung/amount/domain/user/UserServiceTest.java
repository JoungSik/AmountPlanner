package com.joung.amount.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserServiceTest {

    private final UserService userService;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    @Transactional
    @DisplayName("유저 생성")
    void addUser() {
        String email = "example@example.com";
        String password = "qwer1234";

        UserDto.Request request = new UserDto.Request();
        request.setEmail(email);
        request.setPassword(password);

        User user = userService.addUser(request);

        assertNotNull(user.getId());
        assertEquals(email, user.getEmail());
    }
}