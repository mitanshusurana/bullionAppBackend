package com.example.bullion.Controller;


import com.example.bullion.model.User;
import com.example.bullion.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class controller {

    @Autowired
    Service service;

    @GetMapping("/parties")
    public List<User> getNames() {
        return service.getNames();
    }

    @PostMapping("/parties")
    public User createParty(@RequestBody User user) {
        try {
            return service.createParty(user);
        } catch (DuplicateKeyException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "User with name '" + user.getName() + "' already exists."
            );
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unexpected error: " + e.getMessage()
            );
        }
    }
}
