package com.houkstead.ticketsystem.models;


import com.houkstead.ticketsystem.repository.RoleRepository;
import com.houkstead.ticketsystem.repository.UserRepository;
import com.houkstead.ticketsystem.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        userRepository.save(user);

    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public UserServiceImpl(){}

}

