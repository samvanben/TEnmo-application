package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    // Creates a transfer which gets added to the transfer table
    @Override
    public Transfers createTransfer(Transfers transfers, Principal principal) {
        transfers.setAccount_from(principal.getName());
        String sql = "INSERT INTO transfer(account_from, amount, account_to) " +
                "VALUES(?, ?, ?) RETURNING transfer_id;";
        int transfer_id = jdbcTemplate.queryForObject(sql, Integer.class,
                principal.getName(), transfers.getAmount(), transfers.getAccount_to());

        transfers.setTransfer_id(transfer_id);
        return transfers;
    }

//    @Override
//    public Transfers createTransfer(Transfers transfers, int currentUserId) {
//        String sql = "INSERT INTO transfer(account_from, amount, account_to) " +
//                "VALUES(?, ?, ?) RETURNING transfer_id;";
//        int transfer_id = jdbcTemplate.queryForObject(sql, Integer.class,
//                currentUserId, transfers.getAmount(), transfers.getAccount_to());
//
//        transfers.setTransfer_id(transfer_id);
//        return transfers;
//    }

    // Get a list of transfers from the transfer table where account_to and
    // account_from equal the username
    @Override
    public List<Transfers> getTransfersByUserId(Principal principal) {
        List<Transfers> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, account_from, amount, account_to " +
                "FROM transfer WHERE account_from = ? OR account_to = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, principal.getName(), principal.getName());

            while(results.next()){
                Transfers transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
        return transfers;
    }

    @Override
    public Transfers getTransferByTransferId(int transferId) {
        Transfers transfers = null;
       String sql = "SELECT transfer_id, account_from, amount, account_to " +
               "FROM transfer WHERE transfer_id = ?;";
       try{
           SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
           if (results.next()){
               transfers = mapRowToTransfer(results);
           }
       } catch (Exception e){
           System.out.println("Transfer ID does not exist.");
       }
       return transfers;

    }

    private Transfers mapRowToTransfer(SqlRowSet queryResult) {
        Transfers transfers = new Transfers();
        transfers.setTransfer_id(queryResult.getInt("transfer_id"));
        transfers.setAccount_from(queryResult.getString("account_from"));
        transfers.setAmount(queryResult.getBigDecimal("amount"));
        transfers.setAccount_to(queryResult.getString("account_to"));
        return transfers;
    }


}
