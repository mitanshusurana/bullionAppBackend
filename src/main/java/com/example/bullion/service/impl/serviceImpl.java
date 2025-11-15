package com.example.bullion.service.impl;

import com.example.bullion.model.DaybookResponse;
import com.example.bullion.model.Transaction;
import com.example.bullion.model.TransactionType;
import com.example.bullion.model.User;
import com.example.bullion.repo.TransRepo;
import com.example.bullion.repo.UserRepo;
import com.example.bullion.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@org.springframework.stereotype.Service
public class serviceImpl implements Service {

  @Autowired
  UserRepo userRepo;

  @Autowired
  TransRepo transRepo;

  @Autowired
  private MongoTemplate mongoTemplate;
  @Override
  public List<User> getNames() {
    return userRepo.findAll();
  }

  @Override
  public User createParty(User user)
  {

    return userRepo.insert(user);
  }

  @Override
  public Transaction createTransaction(Transaction transaction) {

    TransactionType type = transaction.getType();
    Query query = new Query(Criteria.where("_id").is(transaction.getName()));
    Update update = new Update();

    switch (type) {
      case sale, purchase, cashin, cashout -> {
        BigDecimal cashChange = new BigDecimal(transaction.getBalance().toString());
        update.inc("cashBalance", cashChange);
      }

      case metalin, metalout -> {
        BigDecimal metalChange = new BigDecimal(transaction.getNetWt().toString());
        if (type == TransactionType.metalin) {
          metalChange = metalChange.negate();
        }
        update.inc("metalBalance", metalChange);
      }

      case ratecutPurchase -> {
        // Treat cash like sale/purchase (handled by balance), metal should increase
        BigDecimal cashChange = new BigDecimal(transaction.getBalance().toString());
        BigDecimal metalChange = new BigDecimal(transaction.getNetWt().toString());
        update.inc("cashBalance", cashChange);
        update.inc("metalBalance", metalChange);
      }

      case rateCutsales -> {
        // Treat cash like sale/purchase (handled by balance), metal should decrease
        BigDecimal cashChange = new BigDecimal(transaction.getBalance().toString());
        BigDecimal metalChange = new BigDecimal(transaction.getNetWt().toString()).negate();
        update.inc("cashBalance", cashChange);
        update.inc("metalBalance", metalChange);
      }

      default -> throw new IllegalArgumentException("Unsupported transaction type: " + type);
    }


    mongoTemplate.updateFirst(query, update, User.class);
    return transRepo.insert(transaction);
  }


  @Override
  public List<Transaction> getAllTransaction() {
    return transRepo.findAll();
  }

  @Override
  public List<Transaction> getAllTransactionById(String party) {
    return transRepo.findAllByName(party);
  }


  public DaybookResponse getDaybook(String date) {

    List<Transaction> transactions = transRepo.findAllByDate(date);

    int saleTotal = transactions.stream()
      .filter(t -> "sale".equalsIgnoreCase(t.getType().name()))
      .mapToInt(t -> t.getAmount() != null ? t.getAmount() : 0)
      .sum();

    int purchaseTotal = transactions.stream()
      .filter(t -> "purchase".equalsIgnoreCase(t.getType().name()))
      .mapToInt(t -> t.getAmount() != null ? t.getAmount() : 0)
      .sum();

    int cashInTotal = transactions.stream()
      .mapToInt(t -> t.getCashIn() != null ? t.getCashIn() : 0)
      .sum();

    int cashIn =  transactions.stream()
      .filter(t -> "cashin".equalsIgnoreCase(t.getType().name()))
      .mapToInt(t -> t.getAmount() != null ? t.getAmount() : 0)
      .sum();
    cashInTotal += cashIn;
    int cashOutTotal = transactions.stream()
      .mapToInt(t -> t.getCashOut() != null ? t.getCashOut() : 0)
      .sum();

    int cashOut =transactions.stream()
      .filter(t -> "cashout".equalsIgnoreCase(t.getType().name()))
      .mapToInt(t -> t.getAmount() != null ? t.getAmount() : 0)
      .sum();
    cashOutTotal += cashOut;

    BigDecimal metalPurchasedTotal = transactions.stream()
      .filter(t -> "purchase".equalsIgnoreCase(t.getType().name()))
      .map(t -> t.getNetWt() != null ? t.getNetWt() : BigDecimal.ZERO)
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal metalSoldTotal = transactions.stream()
      .filter(t -> "sale".equalsIgnoreCase(t.getType().name()))
      .map(t -> t.getNetWt() != null ? t.getNetWt() : BigDecimal.ZERO)
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal metalInTotal = transactions.stream()
      .filter(t -> "metalin".equalsIgnoreCase(t.getType().name()))
      .map(t -> t.getNetWt() != null ? t.getNetWt() : BigDecimal.ZERO)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
    BigDecimal metalOutTotal = transactions.stream()
      .filter(t -> "metalout".equalsIgnoreCase(t.getType().name()))
      .map(t -> t.getNetWt() != null ? t.getNetWt() : BigDecimal.ZERO)
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal net = metalPurchasedTotal
      .add(metalInTotal)
      .subtract(metalSoldTotal)
      .subtract(metalOutTotal);

    // Map transactions to DaybookEntry DTO if needed

    // Build response
    DaybookResponse response = new DaybookResponse();
    response.setDate(date.toString());
    response.setEntries(transactions);
    response.setTotals(new DaybookResponse.Totals(saleTotal, purchaseTotal, cashInTotal, cashOutTotal, net));

    return response;
  }
}
