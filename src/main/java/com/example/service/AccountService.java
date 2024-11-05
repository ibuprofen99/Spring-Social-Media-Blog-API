package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.repository.AccountRepository;
import com.example.entity.Account;
import com.example.exception.*;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account registerAccount(Account account) throws DuplicateUsernameException, InvalidRegistrationException {
        //input fields
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            throw new InvalidRegistrationException("Username cannot be blank.");
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new InvalidRegistrationException("Password must be at least 4 characters long.");
        }

        //check for existing username
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new DuplicateUsernameException("Username already exists.");
        }

        return accountRepository.save(account);
    }
    

    // Login with username and password
    public Account login(String username, String password) {
        Optional<Account> account = accountRepository.findByUsername(username);
        return account.filter(acc -> acc.getPassword().equals(password)).orElse(null);
    }
    
    public Optional<Account> getAccountById(Integer accountId) {
        return accountRepository.findById(accountId);
    }

    public Iterable<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
