package com.example.bullion.repo;

import com.example.bullion.model.User;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<User, String> {
}
