package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import static org.mockito.Mockito.*;

public class JdbcTransferDaoTest extends BaseDaoTests {
    private static final Transfers TRANSFER_1 = new Transfers("bob", BigDecimal.valueOf(50.00), "user");
    private static final Transfers TRANSFER_2 = new Transfers("user", BigDecimal.valueOf(250.00), "bob");
    private Transfers testTransfer;
    private JdbcTransferDao sut;
    @Mock
    private Principal mockPrinciple;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
        testTransfer = new Transfers();
        testTransfer.setAccount_from("user");
        testTransfer.setAmount(BigDecimal.valueOf(500.00));
        testTransfer.setAccount_to("bob");
    }

    @Test
    public void createTransfer_adds_new_transfer_to_transfer_table() {
        when(mockPrinciple.getName()).thenReturn("user");
        Transfers createdTransfer = sut.createTransfer(testTransfer, mockPrinciple);
        Assert.assertNotNull(createdTransfer);
        Assert.assertEquals(testTransfer.getAccount_from(), createdTransfer.getAccount_from());
        Assert.assertEquals(testTransfer.getAccount_to(), createdTransfer.getAccount_to());
        Assert.assertEquals(testTransfer.getAmount(), createdTransfer.getAmount());
    }
}