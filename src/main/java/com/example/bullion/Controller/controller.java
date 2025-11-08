package com.example.bullion.Controller;


import com.example.bullion.model.DaybookResponse;
import com.example.bullion.model.Transaction;
import com.example.bullion.model.User;
import com.example.bullion.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Date;
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

    @PostMapping("/transactions")
    public Transaction createTransaction(@RequestBody Transaction transaction){
        return service.createTransaction(transaction);
    }

    @GetMapping("/transactions")
    public List<Transaction> getTransactions(@RequestParam(required = false) String party) {
        if (party != null && !party.isEmpty()) {
            return service.getAllTransactionById(party);
        } else {
            return service.getAllTransaction();
        }
    }

  @GetMapping("/daybook")
  public DaybookResponse getDaybook(
    @RequestParam("date") String date) {

    // Calls service to get transactions for the day
    return service.getDaybook(date);
  }

}
