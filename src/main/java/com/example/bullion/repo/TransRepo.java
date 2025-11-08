package com.example.bullion.repo;

import com.example.bullion.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransRepo extends MongoRepository<Transaction, String> {
    List<Transaction> findAllByName(String name);
    List<Transaction> findAllByDate(String date);

}
