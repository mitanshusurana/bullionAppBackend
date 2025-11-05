package com.example.bullion.repo;

import com.example.bullion.model.Transaction;
import com.example.bullion.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface TransRepo extends MongoRepository<Transaction, String> {
    List<Transaction> findAllByName(String name);

    List<Transaction> findByCreatedAtBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
