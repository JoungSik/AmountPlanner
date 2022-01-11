package com.joung.amount.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto.Response createUser(@RequestBody UserDto.Request request) {
        User user = userService.addUser(request);
        return new UserDto.Response(user);
    }

}
