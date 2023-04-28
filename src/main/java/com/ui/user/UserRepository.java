package com.ui.user;

import org.springframework.data.repository.Repository;

import java.util.List;

interface UserRepository extends Repository<User, String> {

    List<User> findAll();
}
