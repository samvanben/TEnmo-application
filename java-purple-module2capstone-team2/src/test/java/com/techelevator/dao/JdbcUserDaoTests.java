package com.techelevator.dao;


import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UsernamesDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.security.Principal;
import java.util.List;

public class JdbcUserDaoTests extends BaseDaoTests{

    private static final User USER_1 = new User(1001, "bob", "bob", "true");
    private static final User USER_2 = new User(1002, "user", "user", "true");
    private JdbcUserDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
    }

    @Test
    public void createNewUser() {
        boolean userCreated = sut.create("TEST_USER","test_password");
        Assert.assertTrue(userCreated);
        User user = sut.findByUsername("TEST_USER");
        Assert.assertEquals("TEST_USER", user.getUsername());
    }

    @Test
    public void findIdByUsername_returns_correct_userId(){
        int user = sut.findIdByUsername("user");
        Assert.assertEquals(USER_2.getId(), user);

        int user2 = sut.findIdByUsername("bob");
        Assert.assertNotEquals(USER_2.getId(), user2);
    }

    @Test
    public void findByUsername_returns_correct_username(){
        User user = sut.findByUsername("bob");
        assertUserMatch(USER_1, user);

        User user2 = sut.findByUsername("user");
        Assert.assertNotEquals(USER_1, user2);
    }

    @Test
    public void findAllExceptCurrent_returns_correct_users(){
        List<UsernamesDTO> users = sut.findAllExceptCurrent(1001);
        Assert.assertEquals(1, users.size());
        UsernamesDTO expectedUser = new UsernamesDTO();
        expectedUser.setUsername(USER_2.getUsername());
        UsernamesDTO actualUser = users.get(0);
        Assert.assertEquals(expectedUser.getUsername(), actualUser.getUsername());
    }

    private void assertUserMatch(User expected, User actual) {
        Assert.assertEquals(expected.getUsername(), actual.getUsername());
    }
}
