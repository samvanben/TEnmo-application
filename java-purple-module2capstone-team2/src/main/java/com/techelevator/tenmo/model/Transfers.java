package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfers {
    private int transfer_id;
    private String account_from;
    private BigDecimal amount;
    private String account_to;

    public Transfers() {}

    public Transfers(String account_from, BigDecimal amount, String account_to) {
        this.account_from = account_from;
        this.amount = amount;
        this.account_to = account_to;
    }

    public int getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(int transfer_id) {
        this.transfer_id = transfer_id;
    }

    public String getAccount_from() {
        return account_from;
    }

    public void setAccount_from(String account_from) {
        this.account_from = account_from;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAccount_to() {
        return account_to;
    }

    public void setAccount_to(String account_to) {
        this.account_to = account_to;
    }

    public void setStatus(String approved) {
    }

    @Override
    public String toString() {
        return "{\n" +
                "transfer_id: " + transfer_id +
                ", \naccount_from: " + account_from +
                ", \namount: " + amount +
                ", \naccount_to: " + account_to +
                "\n}";
    }
}
