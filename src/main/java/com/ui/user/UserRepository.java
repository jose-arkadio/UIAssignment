package com.ui.user;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface UserRepository extends Repository<User, String> {

    List<User> findAll();

    List<User> findByFullName(String fullName);

    Optional<User> findByAcct(String acct);
}
