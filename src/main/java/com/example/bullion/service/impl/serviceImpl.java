package com.example.bullion.service.impl;

import com.example.bullion.model.User;
import com.example.bullion.repo.UserRepo;
import com.example.bullion.service.Service;
import com.mongodb.DuplicateKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@org.springframework.stereotype.Service
public class serviceImpl implements Service {

    @Autowired
    UserRepo userRepo;
    @Override
    public List<User> getNames() {
        return userRepo.findAll();
    }

    @Override
    public User createParty(User user)
    {

        return userRepo.insert(user);
}
}
