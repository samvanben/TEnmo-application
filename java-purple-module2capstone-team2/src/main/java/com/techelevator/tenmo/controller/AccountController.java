package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UsernamesDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AccountController {

    private UserDao userDao;
    private AccountDao accountDao;

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @GetMapping(path = "/balance")
    @PreAuthorize("isAuthenticated()")
    public Map<String, Object> getAccountBalance(Authentication authentication) {
        String username = authentication.getName();
        User user = userDao.findByUsername(username);
        BigDecimal accountBalance = accountDao.getAccountBalance(user.getId());
        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("balance", accountBalance);
        return response;
    }

    @GetMapping("/send")
    @PreAuthorize("isAuthenticated()")
    public List<UsernamesDTO> getUsersToSendMoneyTo(Principal principal) {
        String currentUsername = principal.getName();
        User currentUser = userDao.findByUsername(currentUsername);
        int currentUserId = currentUser.getId();
        return userDao.findAllExceptCurrent(currentUserId);
    }
}

