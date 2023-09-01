package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfers;

import java.security.Principal;
import java.util.List;

public interface TransferDao {

    Transfers createTransfer(Transfers transfers, Principal principal);

    List<Transfers> getTransfersByUserId(Principal principal);

    Transfers getTransferByTransferId(int id);
}
