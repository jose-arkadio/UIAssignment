package com.ui.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    ResponseEntity<List<BasicUserDto>> getAllUsers(@RequestParam Optional<String> fullName) {

        List<BasicUserDto> users = new ArrayList<>();

        if (fullName.isPresent()) {
            users.addAll(userService.findUsersByFullName(fullName.get()));
        } else {
            users.addAll(userService.getAllUsers());
        }

        return ResponseEntity.ok(users);
    }

}
