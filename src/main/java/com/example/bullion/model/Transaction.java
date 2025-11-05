package com.example.bullion.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDateTime;

@Data
@Document(collection = "Transaction")
public class Transaction {
    TransactionType type;
    private String name;
    private String date;
    private String note;
    private BigDecimal grossWt;
    private BigDecimal purity;
    private BigDecimal netWt;
    private Integer rate;
    private Integer amount;
    private Integer cashIn;
    private Integer cashOut;
    private Integer balance;

    @Id
    private String id;

    private LocalDateTime createdAt;

}
