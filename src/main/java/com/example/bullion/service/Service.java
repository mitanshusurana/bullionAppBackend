package com.example.bullion.service;


import com.example.bullion.model.User;

import java.util.List;

public interface Service {
    public List<User> getNames();

    public User createParty(User user);
}
