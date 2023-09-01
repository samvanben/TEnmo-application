package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UsernamesDTO;

import java.util.List;

public interface UserDao {

    List<UsernamesDTO> findAllExceptCurrent(int currentUserId);

    List<User> findAll();

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);

}
