package com.joung.amount.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User addUser(UserDto.Request request) {
        try {
            return userRepository.save(User.builder()
                    .email(request.getEmail())
                    .password(new BCryptPasswordEncoder().encode(request.getPassword()))
                    .build());
        } catch (DataIntegrityViolationException exception) {
            return null;
        }
    }

}
