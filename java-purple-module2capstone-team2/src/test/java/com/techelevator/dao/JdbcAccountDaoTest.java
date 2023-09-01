package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTest extends BaseDaoTests{

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void getAccountBalance_returns_correct_balance() {
        BigDecimal expectedBalance = new BigDecimal("500.00");
        BigDecimal actualBalance = sut.getAccountBalance(1002);
        Assert.assertEquals(expectedBalance, actualBalance);

        BigDecimal expectedBalance2 = new BigDecimal("500.00");
        BigDecimal actualBalance2 = sut.getAccountBalance(1001);
        Assert.assertNotEquals(expectedBalance2, actualBalance2);
    }

}
