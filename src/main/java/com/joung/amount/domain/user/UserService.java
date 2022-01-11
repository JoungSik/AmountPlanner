package com.joung.amount.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(UserDto.Request request) {
        return userRepository.save(User.builder()
                .email(request.getEmail())
                .password(new BCryptPasswordEncoder().encode(request.getPassword()))
                .build());
    }

}
