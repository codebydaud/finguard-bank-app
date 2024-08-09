package com.codebydaud.training.banking_app.repository;

import com.codebydaud.training.banking_app.entity.Account;
import com.codebydaud.training.banking_app.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findByToken(String token);

    Token[] findAllByAccount(Account account);

    void deleteByToken(String token);
}