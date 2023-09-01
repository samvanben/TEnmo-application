package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UsernamesDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController

public class TransferController {
     private TransferDao transferDao;
     private AccountDao accountDao;
     private UserDao userDao;

     public TransferController(TransferDao transferDao, AccountDao accountDao, UserDao userDao) {
         this.transferDao = transferDao;
         this.accountDao = accountDao;
         this.userDao = userDao;
     }

     @PostMapping("/transfer")
     @PreAuthorize("isAuthenticated()")
     public ResponseEntity<String> createTransfer(@RequestBody Transfers transfer, Principal principal) {
         String currentUsername = principal.getName();
         User currentUser = userDao.findByUsername(currentUsername);
         int currentUserId = currentUser.getId();
         BigDecimal senderBalance = accountDao.getAccountBalance(currentUserId);
         BigDecimal transferAmount = transfer.getAmount();

         // Check that user is not sending money to themselves
         if (principal.getName().equals(transfer.getAccount_to())){
             return new ResponseEntity<>("Unable to send money to self", HttpStatus.BAD_REQUEST);
         }

         // Check for negative or zero transfer amount if
         if (transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
             return new ResponseEntity<>("Transfer amount must be a positive value",
                 HttpStatus.BAD_REQUEST); }

         // Check if sender has sufficient funds
         if (transferAmount.compareTo(senderBalance) > 0) {
             return new ResponseEntity<>("Insufficient funds", HttpStatus.BAD_REQUEST); }

         // Calculate new balances
         BigDecimal newSenderBalance = senderBalance.subtract(transferAmount);
         BigDecimal receiverBalance = accountDao.getAccountBalance(userDao.findIdByUsername(transfer.getAccount_to()));
         BigDecimal newReceiverBalance = receiverBalance.add(transferAmount);

         // Update sender's balance
         accountDao.updateAccountBalance(currentUserId, newSenderBalance);

         // Update receiver's balance
         accountDao.updateAccountBalance(userDao.findIdByUsername(transfer.getAccount_to()), newReceiverBalance);

         // Set initial status of the transfer to Approved
         transfer.setStatus("Approved");

         // Create transfer
         Transfers createdTransfer = transferDao.createTransfer(transfer, principal);
         return new ResponseEntity<>("Transfer successful\n" + transfer.toString(), HttpStatus.CREATED);

     }

     @GetMapping("/transfer/log")
    @PreAuthorize("isAuthenticated()")
    public List<Transfers> getTransfersByUserId(Principal principal){
        String currentUsername = principal.getName();
        User currentUser = userDao.findByUsername(currentUsername);
        int currentUserId = currentUser.getId();
        return transferDao.getTransfersByUserId(principal);
     }


     @GetMapping("/transfer/log/{id}")
    @PreAuthorize("isAuthenticated()")
    public Transfers getTransferByTransferId(@PathVariable int id){
         Transfers transfers = transferDao.getTransferByTransferId(id);
         if (transfers == null){
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found.");
         } else {
             return transfers;
         }
     }
 }

