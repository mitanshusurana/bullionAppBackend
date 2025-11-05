package com.example.bullion.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Data
@Document(collection = "users")
public class User {

  @Id
  private String name;

  @Field(targetType = org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128)
  private BigDecimal metalBalance = BigDecimal.ZERO;

  @Field(targetType = org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128)
  private BigDecimal cashBalance = BigDecimal.ZERO;
}
