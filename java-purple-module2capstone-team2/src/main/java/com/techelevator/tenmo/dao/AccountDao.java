package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfers;

import java.math.BigDecimal;

public interface AccountDao {
    BigDecimal getAccountBalance(int user_id);

    void updateAccountBalance(int currentUserId, BigDecimal newSenderBalance);



}
