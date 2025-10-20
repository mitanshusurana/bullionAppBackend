package com.example.bullion.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String name;

    private BigDecimal metalBalance = BigDecimal.ZERO;
    private BigDecimal cashBalance = BigDecimal.ZERO;
}
