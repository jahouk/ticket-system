package com.houkstead.ticketsystem;

import com.houkstead.ticketsystem.models.User;

import java.util.List;

public interface UserService {
    public User findUserByUsername(String username);
    public void saveUser(User user);
    public List<User> findAll();
    public void updateUser(User user);
    public void updateUser(User user, String password);
}