package com.ui.user;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<BasicUserDto> getAllUsers() {

        List<User> allUsers = userRepository.findAll();

        return allUsers.stream()
                .map(User::toBasicUserDto)
                .collect(Collectors.toList());
    }

    public List<BasicUserDto> findUsersByFullName(String fullName) {

        List<User> foundUsers = userRepository.findByFullName(fullName);

        if (foundUsers.isEmpty()) {
            throw new ResourceNotFoundException("No users [fullname=" + fullName + "] have been found");
        }

        return foundUsers.stream()
                .map(User::toBasicUserDto)
                .collect(Collectors.toList());
    }

}
