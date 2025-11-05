package com.example.bullion.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class DaybookResponse {
  private String date;
  private List<Transaction> entries;
  private Totals totals;

  @Data
  @AllArgsConstructor
  public static class Totals {
    private int sale;
    private int purchase;
    private int cashin;
    private int cashout;
    private BigDecimal net;
  }
}


